package com.mdd.controller;

import com.mdd.dto.CommentDto;
import com.mdd.model.Comment;
import com.mdd.model.CustomUserDetails;
import com.mdd.model.Post;
import com.mdd.model.User;
import com.mdd.service.CommentService;
import com.mdd.service.PostService;
import com.mdd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/post/{postId}")
    public List<CommentDto> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId).stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getUsername(),
                        comment.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.getUserByEmail(userDetails.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (comment.getPost() == null || comment.getPost().getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Optional<Post> postOptional = postService.getPostById(comment.getPost().getId());
        if (postOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Post post = postOptional.get();
        comment.setPost(post);
        comment.setUser(user);

        Comment savedComment = commentService.createComment(comment);

        CommentDto commentDto = new CommentDto(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getUser().getUsername(),
                savedComment.getCreatedAt()
        );
        return ResponseEntity.ok(commentDto);
    }
}
