package com.blog.services.impl;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.CategoryNotFoundException;
import com.blog.exceptions.PostNotFoundException;
import com.blog.exceptions.UserNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.repositories.CategoryRepository;
import com.blog.repositories.PostRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto, String userId, String categoryId) {
        Post post = this.modelMapper.map(postDto, Post.class);

        User user = this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        post.setCreatedOn(new Date());
        post.setUser(user);
        post.setCategory(category);
        Post savedPost = this.postRepository.save(post);

        return this.modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public PostDto getPostById(String postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found!"));
        return this.modelMapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = this.postRepository.findAll();
        return posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
    }

    @Override
    public void deletePost(String postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found!", postId)));
        postRepository.delete(post);
    }
}
