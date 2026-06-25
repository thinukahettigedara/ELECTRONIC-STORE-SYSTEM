package com.nanolap.controller;

import com.nanolap.model.User;
import com.nanolap.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");
        String role = request.get("role");

        System.out.println("Registration attempt - Name: " + name + ", Email: " + email + ", Role: " + role);

        if (name == null || email == null || password == null || role == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Name, email, password, and role are required"));
        }

        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(409).body(Map.of("message", "Email already registered"));
        }

        // Create new user
        User user = new User(name, email, password, role);
        System.out.println("User created before save - Role: " + user.getRole());
        User savedUser = userRepository.save(user);
        System.out.println("User saved - ID: " + savedUser.getId() + ", Role: " + savedUser.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registration successful");
        response.put("user", Map.of(
            "id", savedUser.getId(),
            "name", savedUser.getName(),
            "email", savedUser.getEmail(),
            "role", savedUser.getRole()
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String role = request.get("role"); // Optional

        System.out.println("Login attempt - Email: " + email + ", Password length: " + (password != null ? password.length() : "null"));

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required"));
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            System.out.println("User not found for email: " + email);
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }

        User user = userOpt.get();
        System.out.println("User found - ID: " + user.getId() + ", Role: " + user.getRole() + ", DB Password length: " + user.getPassword().length());

        // Check if role matches (if provided)
        if (role != null && !role.isEmpty() && !user.getRole().equalsIgnoreCase(role)) {
            System.out.println("Role mismatch - Expected: " + user.getRole() + ", Provided: " + role);
            return ResponseEntity.status(403).body(Map.of("message", "Access denied. Invalid role for this account."));
        }

        // Simple password check (in production, use BCrypt)
        System.out.println("Password comparison - Input: '" + password + "', DB: '" + user.getPassword() + "', Match: " + user.getPassword().equals(password));
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }

        // Create response with user data (excluding password)
        Map<String, Object> response = new HashMap<>();
        response.put("token", "simple-token-" + user.getId()); // Simple token for demo
        response.put("user", Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "email", user.getEmail(),
            "role", user.getRole(),
            "phone", user.getPhone() != null ? user.getPhone() : "",
            "address", user.getAddress() != null ? user.getAddress() : ""
        ));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestHeader(value = "Authorization", required = false) String authorization,
                                           @RequestBody Map<String, String> request) {
        // Simple token validation (in production, use JWT)
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }

        String token = authorization.substring(7);
        if (!token.startsWith("simple-token-")) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid token"));
        }

        try {
            Long userId = Long.parseLong(token.substring("simple-token-".length()));
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("message", "User not found"));
            }

            User user = userOpt.get();
            
            if (request.containsKey("name")) {
                user.setName(request.get("name"));
            }
            if (request.containsKey("phone")) {
                user.setPhone(request.get("phone"));
            }
            if (request.containsKey("address")) {
                user.setAddress(request.get("address"));
            }

            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profile updated successfully");
            response.put("user", Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "phone", user.getPhone() != null ? user.getPhone() : "",
                "address", user.getAddress() != null ? user.getAddress() : ""
            ));

            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid token"));
        }
    }
}
