package com.blog.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private String title;

    private String content;

    private String image;

    private Date createdOn;

    private CategoryDto category;

    private UserDto user;

    private Set<CommentDto> comments;
}
