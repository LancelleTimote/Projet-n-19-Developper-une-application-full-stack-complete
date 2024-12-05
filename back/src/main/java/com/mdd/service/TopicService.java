package com.mdd.service;

import com.mdd.model.Topic;
import com.mdd.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }

    public Optional<Topic> getTopicByName(String name) {
        return topicRepository.findByName(name);
    }

    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public Topic updateTopic(Long id, Topic updatedTopic) {
        return topicRepository.findById(id)
                .map(existingTopic -> {
                    existingTopic.setName(updatedTopic.getName());
                    existingTopic.setDescription(updatedTopic.getDescription());
                    return topicRepository.save(existingTopic);
                }).orElseThrow(() -> new RuntimeException("Topic not found"));
    }

    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }
}
