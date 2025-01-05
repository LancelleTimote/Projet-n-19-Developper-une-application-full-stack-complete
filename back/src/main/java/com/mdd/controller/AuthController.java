package com.mdd.controller;

import com.mdd.dto.UserDto;
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

        UserDto userDto = new UserDto(registeredUser.getId(), registeredUser.getUsername(), registeredUser.getEmail(), null);

        return ResponseEntity.ok(new AuthResponse(jwtToken, userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        final String jwtToken = jwtUtil.generateToken(userService.loadUserByUsername(user.getEmail()));

        UserDto userDto = new UserDto(loggedInUser.getId(), loggedInUser.getUsername(), loggedInUser.getEmail(), null);

        AuthResponse response = new AuthResponse(jwtToken, userDto);

        return ResponseEntity.ok(response);
    }
}