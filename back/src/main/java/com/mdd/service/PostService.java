package com.mdd.service;

import com.mdd.dto.PostDto;
import com.mdd.model.Post;
import com.mdd.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PostDto convertToDto(Post post) {
        String authorUsername = post.getAuthor() != null ? post.getAuthor().getUsername() : "Utilisateur inconnu";
        String topicName = post.getTopic() != null ? post.getTopic().getName() : "Article inconnu";

        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                authorUsername,
                topicName,
                post.getCreatedAt()
        );
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }
}
