package com.nanolap.controller;

import com.nanolap.model.Order;
import com.nanolap.model.User;
import com.nanolap.repository.UserRepository;
import com.nanolap.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    // POST create order (logged-in customers only)
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody Map<String, Object> orderRequest) {
        try {
            Optional<User> userOpt = getUserFromAuthorization(authorization);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Please log in to place an order"));
            }

            User user = userOpt.get();
            if (!"customer".equalsIgnoreCase(user.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only customers can place orders"));
            }

            Order order = new Order();
            order.setCustomerName(user.getName());
            order.setCustomerEmail(user.getEmail());

            String phone = (String) orderRequest.get("customerPhone");
            if (phone == null || phone.isBlank()) {
                phone = user.getPhone();
            }
            order.setCustomerPhone(phone);

            String address = (String) orderRequest.get("shippingAddress");
            if (address == null || address.isBlank()) {
                address = user.getAddress();
            }
            order.setShippingAddress(address);
            order.setPaymentMethod((String) orderRequest.get("paymentMethod"));

            @SuppressWarnings("unchecked")
            Map<String, Integer> cartItemsRaw = (Map<String, Integer>) orderRequest.get("cartItems");
            if (cartItemsRaw == null || cartItemsRaw.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Cart is empty"));
            }

            Map<Long, Integer> cartItems = new HashMap<>();
            for (Map.Entry<String, Integer> entry : cartItemsRaw.entrySet()) {
                cartItems.put(Long.parseLong(entry.getKey()), entry.getValue());
            }

            Order saved = orderService.createOrder(order, cartItems);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // GET order by number
    @GetMapping("/{orderNumber}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderNumber) {
        Optional<Order> order = orderService.getOrderByNumber(orderNumber);
        return order.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // GET all orders (admin)
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET orders for logged-in customer
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(@RequestParam(required = false) String email) {
        try {
            if (email == null || email.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }
            
            List<Order> orders = orderService.getOrdersByCustomerEmail(email);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error fetching orders: " + e.getMessage()));
        }
    }

    private Optional<User> getUserFromAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Optional.empty();
        }

        String token = authorization.substring(7);
        if (!token.startsWith("simple-token-")) {
            return Optional.empty();
        }

        try {
            Long userId = Long.parseLong(token.substring("simple-token-".length()));
            return userRepository.findById(userId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    // PUT update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id,
                                               @RequestBody Map<String, String> body) {
        Order updated = orderService.updateOrderStatus(id, body.get("status"));
        return ResponseEntity.ok(updated);
    }
}
