package com.blog.services;
import com.blog.payloads.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserById(Integer userId);

    List<UserDto> getAllUsers();

    void deleteUserById(Integer userId);

    UserDto updateUser(UserDto userDto, Integer userId);

    UserDto registerNewUser(UserDto userDto);
}
