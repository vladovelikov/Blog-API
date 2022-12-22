package com.blog.services.impl;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.CategoryNotFoundException;
import com.blog.exceptions.PostNotFoundException;
import com.blog.exceptions.UserNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.repositories.CategoryRepository;
import com.blog.repositories.PostRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
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
    public PostDto getPostById(Integer postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found!"));
        return this.modelMapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = this.postRepository.findAll();
        return posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found!", postId)));
        postRepository.delete(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found!", postId)));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImage(postDto.getImage());

        Post updatedPost = this.postRepository.save(post);

        return this.modelMapper.map(updatedPost, PostDto.class);
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found!", userId)));
        List<Post> posts = this.postRepository.findByUser(user);

        return posts.stream().map(p -> this.modelMapper.map(p, PostDto.class)).collect(Collectors.toList());
    }

    @Override
    public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize) {
        Pageable pg = PageRequest.of(pageNumber, pageSize);

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(String.format("Category with id %s not found!", categoryId)));

        Page<Post> pagePosts = this.postRepository.findByCategory(category, pg);
        List<Post> posts = pagePosts.getContent();
        List<PostDto> postDtos = posts.stream().map(p -> this.modelMapper.map(p, PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setLastPage(pagePosts.isLast());

        return postResponse;
    }

    @Override
    public List<PostDto> searchPost(String keywords) {
        List<Post> posts = this.postRepository.findByTitleContaining(keywords);
        return posts.stream().map(p -> this.modelMapper.map(p, PostDto.class)).collect(Collectors.toList());
    }

    @Override
    public PostResponse getAllPostsByPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = null;

        if (sortBy.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pg = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> pagePosts = this.postRepository.findAll(pg);
        List<Post> posts = pagePosts.getContent();

        List<PostDto> postDtos = posts.stream().map(p -> this.modelMapper.map(p, PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setLastPage(pagePosts.isLast());

        return postResponse;
    }
}
