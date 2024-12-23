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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;
    @GetMapping
    public List<Map<String, Object>> getUserSubscriptions() {
        String email = getAuthenticatedEmail();
        List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(email);

        return subscriptions.stream().map(subscription -> {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("topicId", subscription.getTopic().getId());
            result.put("topicTitle", subscription.getTopic().getName());
            result.put("topicDescription", subscription.getTopic().getDescription());
            return result;
        }).collect(Collectors.toList());
    }

    @PostMapping("/{topicId}")
    public ResponseEntity<Void> subscribeToTopic(@PathVariable Long topicId) {
        String email = getAuthenticatedEmail();
        try {
            subscriptionService.subscribeToTopic(email, topicId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (AlreadySubscribedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<String> unsubscribeFromTopic(@PathVariable Long topicId) {
        String email = getAuthenticatedEmail();
        try {
            subscriptionService.unsubscribeFromTopic(email, topicId);
            return new ResponseEntity<>("Successfully unsubscribed from topic", HttpStatus.OK);
        } catch (UserNotFoundException | TopicNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private String getAuthenticatedEmail() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }
}
