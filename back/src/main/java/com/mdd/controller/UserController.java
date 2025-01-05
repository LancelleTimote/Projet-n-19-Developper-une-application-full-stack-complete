package com.mdd.controller;

import com.mdd.dto.UserDto;
import com.mdd.exception.UserNotFoundException;
import com.mdd.model.CustomUserDetails;
import com.mdd.model.User;
import com.mdd.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<User> updateUser(@RequestBody UserDto userDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            User updatedUser = userService.updateUser(userDto);

            if (!updatedUser.getEmail().equals(userDetails.getUsername())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        updatedUser.getEmail(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
