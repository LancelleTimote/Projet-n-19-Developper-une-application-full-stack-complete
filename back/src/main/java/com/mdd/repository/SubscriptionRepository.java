package com.mdd.repository;

import com.mdd.model.Subscription;
import com.mdd.model.Topic;
import com.mdd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser(User user);
    boolean existsByUserAndTopic(User user, Topic topic);
    void deleteByUserAndTopic(User user, Topic topic);
}
