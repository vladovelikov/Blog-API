package com.blog.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {


    @NotEmpty
    @Size(min = 4, message = "User name should be at least 4 characters long.")
    private String name;

    @Email(message = "Email address is not valid.")
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 8, max = 15, message = "Password should be 8-15 characters long.")
    private String password;

    @NotEmpty
    private String about;

    private Set<CommentDto> comments = new HashSet<>();
}
