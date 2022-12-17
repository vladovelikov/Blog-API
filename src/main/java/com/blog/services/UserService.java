package com.blog.services;
import com.blog.payloads.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserById(String userId);

    List<UserDto> getAllUsers();

    void deleteUserById(String userId);

    UserDto updateUser(UserDto userDto, String userId);

    UserDto registerNewUser(UserDto userDto);
}
