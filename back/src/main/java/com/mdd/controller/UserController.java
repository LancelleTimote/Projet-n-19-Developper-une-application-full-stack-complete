package com.mdd.controller;

import com.mdd.dto.UserDto;
import com.mdd.exception.UserNotFoundException;
import com.mdd.model.CustomUserDetails;
import com.mdd.model.User;
import com.mdd.service.UserService;
import com.mdd.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/me")
    public ResponseEntity<UserDto> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), null);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserDto updatedUserDTO) {
        if (updatedUserDTO.getUsername() != null && !updatedUserDTO.getUsername().isEmpty()) {
            if (!ValidationUtil.isValidUsername(updatedUserDTO.getUsername())) {
                return ResponseEntity.badRequest().body("Invalid username format");
            }
        }

        if (updatedUserDTO.getEmail() != null && !updatedUserDTO.getEmail().isEmpty()) {
            if (!ValidationUtil.isValidEmail(updatedUserDTO.getEmail())) {
                return ResponseEntity.badRequest().body("Invalid email format");
            }
        }

        if (updatedUserDTO.getPassword() != null && !updatedUserDTO.getPassword().isEmpty()) {
            if (!ValidationUtil.isValidPassword(updatedUserDTO.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid password format");
            }
        }

        User existingUser = userService.getUserById(updatedUserDTO.getId());
        existingUser.setUsername(updatedUserDTO.getUsername());
        existingUser.setEmail(updatedUserDTO.getEmail());

        if (updatedUserDTO.getPassword() != null && !updatedUserDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserDTO.getPassword()));
        }

        User updated = userService.updateUser(existingUser);
        return ResponseEntity.ok(updated);
    }
}
