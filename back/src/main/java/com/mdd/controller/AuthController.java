package com.mdd.controller;

import com.mdd.model.CustomUserDetails;
import com.mdd.model.User;
import com.mdd.service.UserService;
import com.mdd.util.JwtUtil;
import com.mdd.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getUsername() == null) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email format is invalid.");
        }

        if (!ValidationUtil.isValidPassword(user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password format. Password must be at least 8 characters long and include an uppercase letter, a lowercase letter, a digit, and a special character.");
        }

        if (!ValidationUtil.isValidUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Invalid username format. Only letters and numbers are allowed, and the length must not exceed 12 characters.");
        }

        User registeredUser = userService.register(user);

        CustomUserDetails userDetails = new CustomUserDetails(registeredUser);
        final String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwtToken, registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        final String jwtToken = jwtUtil.generateToken(userService.loadUserByUsername(user.getEmail()));

        AuthResponse response = new AuthResponse(jwtToken, loggedInUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(user);
    }
}