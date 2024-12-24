package com.mdd.controller;

import com.mdd.exception.UserNotFoundException;
import com.mdd.model.User;
import com.mdd.service.UserService;
import com.mdd.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedUser) {
        if (updatedUser.getId() == null) {
            return ResponseEntity.badRequest().body("User ID must not be null");
        }
        try {
            User existingUser = userService.getUserById(updatedUser.getId());

            if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
                existingUser.setUsername(updatedUser.getUsername());
            }

            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                existingUser.setEmail(updatedUser.getEmail());
            }

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                if (!ValidationUtil.isValidPassword(updatedUser.getPassword())) {
                    return ResponseEntity.badRequest().body("Invalid password format...");
                }
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            User updated = userService.updateUser(existingUser);
            return ResponseEntity.ok(updated);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating profile: " + e.getMessage());
        }
    }
}
