package com.example.GoQuiz.service;

import com.example.GoQuiz.dto.UserDto;
import com.example.GoQuiz.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);
    User getUserByEmail(String email);
    List<User> getAllUsers();
}

