package com.restapiblog.restapiblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapiblog.restapiblog.Dto.UserDto;
import com.restapiblog.restapiblog.model.Users;
import com.restapiblog.restapiblog.serviceImpl.UserServiceImpl;
import com.restapiblog.restapiblog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtils utils;

    @Autowired
    public UserController(@Lazy UserServiceImpl userService, PasswordEncoder passwordEncoder, JwtUtils utils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;
    }

    @PostMapping("/sign-up")
    // responseEntity here is indicating that we want to return Http status code (200(OK), 400, 500 etc.
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto){
        Users user = userService.saveUser(userDto);
        UserDto userDto1 = new ObjectMapper(). convertValue(user, UserDto.class);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> logInUser(@RequestBody UserDto userDto){
        UserDetails user = userService.loadUserByUsername(userDto.getUsername());
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())){
            String jwtToken = utils.createJwt.apply(user);
            return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("username and password is incorrect", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "welcome to your bashboard";
    }
}
