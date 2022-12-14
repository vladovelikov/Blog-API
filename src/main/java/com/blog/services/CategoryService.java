package com.blog.services;

import com.blog.payloads.CategoryDto;

public interface CategoryService {
    CategoryDto getCategoryById(String categoryId);
}
