package com.blog.services.impl;

import com.blog.entities.User;
import com.blog.exceptions.UserNotFoundException;
import com.blog.payloads.UserDto;
import com.blog.repositories.UserRepository;
import com.blog.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        User createdUser = this.userRepository.save(user);
        return this.modelMapper.map(createdUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(u -> this.modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(String userId) {
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());

        this.userRepository.save(user);
        return this.modelMapper.map(user, UserDto.class);
    }
}
