package com.mdd.service;

import com.mdd.model.Subscription;
import com.mdd.model.Topic;
import com.mdd.model.User;
import com.mdd.repository.SubscriptionRepository;
import com.mdd.repository.TopicRepository;
import com.mdd.repository.UserRepository;
import com.mdd.exception.UserNotFoundException;
import com.mdd.exception.TopicNotFoundException;
import com.mdd.exception.AlreadySubscribedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    public List<Subscription> getUserSubscriptions(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));
        return subscriptionRepository.findByUser(user);
    }

    public void subscribeToTopic(String username, Long topicId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Topic with ID " + topicId + " not found"));

        if (subscriptionRepository.existsByUserAndTopic(user, topic)) {
            throw new AlreadySubscribedException("User is already subscribed to this topic");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setTopic(topic);
        subscriptionRepository.save(subscription);
    }

    public void unsubscribeFromTopic(String username, Long topicId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Topic with ID " + topicId + " not found"));

        subscriptionRepository.deleteByUserAndTopic(user, topic);
    }
}
