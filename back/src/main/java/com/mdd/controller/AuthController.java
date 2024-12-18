package com.mdd.controller;

import com.mdd.model.User;
import com.mdd.service.UserService;
import com.mdd.util.JwtUtil;
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

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        if (!isValidPassword(user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password format. Password must be at least 8 characters long and include an uppercase letter, a lowercase letter, a digit, and a special character.");
        }

        User registeredUser = userService.register(user);
        final String jwtToken = jwtUtil.generateToken(userService.loadUserByUsername(user.getEmail()));

        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody User user) {
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        final String jwtToken = jwtUtil.generateToken(userService.loadUserByUsername(user.getEmail()));
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        User user = userService.getUserByEmail(username);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(user);
    }
}