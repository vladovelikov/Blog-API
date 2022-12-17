package com.blog.controllers;

import com.blog.exceptions.InvalidCredentialsException;
import com.blog.payloads.JwtAuthRequest;
import com.blog.payloads.JwtAuthResponse;
import com.blog.payloads.UserDto;
import com.blog.security.JwtAuthenticationHelper;
import com.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationHelper jwtAuthenticationHelper;

    @Autowired
    public AuthController(UserService userService, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtAuthenticationHelper jwtAuthenticationHelper) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtAuthenticationHelper = jwtAuthenticationHelper;
    }

    @GetMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest request) {
        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

        String token = this.jwtAuthenticationHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        try {
            this.authenticationManager.authenticate(token);
        } catch (InvalidCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerNewUser(@RequestBody UserDto userDto) {
        UserDto newUser = this.userService.registerNewUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
