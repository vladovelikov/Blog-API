package com.blog.services;

import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

    PostDto getPostById(Integer postId);

    List<PostDto> getAllPosts();

    void deletePost(Integer postId);

    PostDto updatePost(PostDto postDto, Integer postId);

    List<PostDto> getPostsByUser(Integer userId);

    PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize);

    List<PostDto> searchPost(String keywords);

    PostResponse getAllPostsByPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
