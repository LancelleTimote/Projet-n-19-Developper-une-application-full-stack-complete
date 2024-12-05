package com.mdd.controller;

import com.mdd.model.Subscription;
import com.mdd.service.SubscriptionService;
import com.mdd.exception.UserNotFoundException;
import com.mdd.exception.TopicNotFoundException;
import com.mdd.exception.AlreadySubscribedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public List<Subscription> getUserSubscriptions() {
        String username = getAuthenticatedUsername();
        return subscriptionService.getUserSubscriptions(username);
    }

    @PostMapping("/{topicId}")
    public ResponseEntity<String> subscribeToTopic(@PathVariable Long topicId) {
        String username = getAuthenticatedUsername();
        try {
            subscriptionService.subscribeToTopic(username, topicId);
            return new ResponseEntity<>("Successfully subscribed to topic", HttpStatus.CREATED);
        } catch (AlreadySubscribedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<String> unsubscribeFromTopic(@PathVariable Long topicId) {
        String username = getAuthenticatedUsername();
        try {
            subscriptionService.unsubscribeFromTopic(username, topicId);
            return new ResponseEntity<>("Successfully unsubscribed from topic", HttpStatus.OK);
        } catch (UserNotFoundException | TopicNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private String getAuthenticatedUsername() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }
}
