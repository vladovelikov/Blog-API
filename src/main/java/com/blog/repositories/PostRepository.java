package com.blog.repositories;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByUser(User user);

    Page<Post> findByCategory(Category category, Pageable pg);

    List<Post> findByTitleContaining(String keywords);
}
