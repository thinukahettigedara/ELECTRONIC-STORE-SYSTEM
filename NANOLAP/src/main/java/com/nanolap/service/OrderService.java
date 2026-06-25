package com.nanolap.service;

import com.nanolap.model.Order;
import com.nanolap.model.OrderItem;
import com.nanolap.model.Product;
import com.nanolap.repository.OrderRepository;
import com.nanolap.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(Order order, Map<Long, Integer> cartItems) {
        BigDecimal total = BigDecimal.ZERO;

        List<OrderItem> items = new java.util.ArrayList<>();
        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Optional<Product> productOpt = productRepository.findById(entry.getKey());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity(entry.getValue());
                item.setUnitPrice(product.getPrice());
                BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                item.setTotalPrice(itemTotal);
                total = total.add(itemTotal);
                items.add(item);
            }
        }

        order.setOrderItems(items);
        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);
        saved.setOrderNumber(String.valueOf(saved.getId()));
        return orderRepository.save(saved);
    }

    public Optional<Order> getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public long getTotalOrderCount() {
        return orderRepository.count();
    }
}
