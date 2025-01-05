package com.mdd.service;

import com.mdd.dto.UserDto;
import com.mdd.exception.UserNotFoundException;
import com.mdd.model.User;
import com.mdd.repository.UserRepository;
import com.mdd.util.ValidationUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent() && passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            return optionalUser.get();
        }
        return null;
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        Hibernate.initialize(user.getSubscriptions());
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    public User updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()) {
            if (!ValidationUtil.isValidUsername(userDto.getUsername())) {
                throw new IllegalArgumentException("Invalid username format");
            }
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            if (!ValidationUtil.isValidEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Invalid email format");
            }
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            if (!ValidationUtil.isValidPassword(userDto.getPassword())) {
                throw new IllegalArgumentException("Invalid password format");
            }
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());
            user.setPassword(encodedPassword);
        }

        return userRepository.save(user);
    }
}
