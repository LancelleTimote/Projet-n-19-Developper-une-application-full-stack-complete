package com.mdd.repository;

import com.mdd.model.Subscription;
import com.mdd.model.Topic;
import com.mdd.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s JOIN FETCH s.topic WHERE s.user = :user")
    List<Subscription> findByUser(@Param("user") User user);
    boolean existsByUserAndTopic(User user, Topic topic);
    void deleteByUserAndTopic(User user, Topic topic);
}
