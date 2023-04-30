package com.example.GoQuiz.service;

import com.example.GoQuiz.config.SecurityConfig;
import com.example.GoQuiz.dto.UserDto;
import com.example.GoQuiz.exceptions.EmailAlreadyExistsException;
import com.example.GoQuiz.exceptions.PasswordTooWeakException;
import com.example.GoQuiz.exceptions.UserNotFoundException;
import com.example.GoQuiz.exceptions.UsernameAlreadyExistsException;
import com.example.GoQuiz.model.User;
import com.example.GoQuiz.repository.UserRepository;
import com.example.GoQuiz.util.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository repository;

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws UserNotFoundException {
        return repository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public void deleteUser(User user) {
        repository.delete(user);
    }
}
