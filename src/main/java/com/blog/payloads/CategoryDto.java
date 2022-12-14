package com.blog.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @NotBlank
    @Size(min = 6, max = 12, message = "Category should be between 6 and 12 characters long.")
    private String title;

    @NotBlank
    @Size(min = 10, message = "Category description should be at least 10 characters long.")
    private String description;
}
