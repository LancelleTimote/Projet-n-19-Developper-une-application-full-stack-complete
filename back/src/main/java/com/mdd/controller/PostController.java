package com.mdd.controller;

import com.mdd.model.Post;
import com.mdd.model.User;
import com.mdd.repository.UserRepository;
import com.mdd.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    private final UserRepository userRepository;

    public PostController(PostService postService, UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, @AuthenticationPrincipal User user) {
        System.out.println("Post reçu: " + post);

        if (post.getAuthor() == null) {
            System.out.println("Aucun auteur associé au post !");
        } else {
            System.out.println("Auteur trouvé: " + post.getAuthor().getId());
        }

        Optional<User> author = userRepository.findById(post.getAuthor().getId());

        if (author.isPresent()) {
            post.setAuthor(author.get());
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }
}
