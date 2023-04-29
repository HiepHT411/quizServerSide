package com.example.GoQuiz.service;

import com.example.GoQuiz.dto.UserDto;
import com.example.GoQuiz.exceptions.EmailAlreadyExistsException;
import com.example.GoQuiz.exceptions.PasswordTooWeakException;
import com.example.GoQuiz.exceptions.UserNotFoundException;
import com.example.GoQuiz.exceptions.UsernameAlreadyExistsException;
import com.example.GoQuiz.model.User;
import com.example.GoQuiz.repository.UserRepository;
import com.example.GoQuiz.util.PasswordValidator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(UserDto userDto) throws UsernameAlreadyExistsException, EmailAlreadyExistsException, PasswordTooWeakException {
        if(repository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("User with username " + userDto.getUsername() + " already exists");
        }

        if(repository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("User with email " + userDto.getEmail() + " already exists");
        }

        if(PasswordValidator.check(userDto.getPassword())) {
            User user = User.from(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            return repository.save(user);
        } else {
            throw new PasswordTooWeakException("Password is too weak");
        }
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
