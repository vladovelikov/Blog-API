package com.blog.controllers;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.PostDto;
import com.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, @PathVariable String userId, @PathVariable String categoryId) {
        PostDto savedPost = this.postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PostDto> updatePost() {

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> deletePostById(@PathVariable String postId) {
        this.postService.deletePost(postId);
        return new ResponseEntity<>(new ApiResponse("Post deleted successfully!", true), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable String postId) {
        PostDto post = this.postService.getPostById(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = this.postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
