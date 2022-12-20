package com.blog.services;

import com.blog.payloads.PostDto;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto, String userId, String categoryId);
    PostDto getPostById(String postId);
    List<PostDto> getAllPosts();
    void deletePost(String postId);
}
