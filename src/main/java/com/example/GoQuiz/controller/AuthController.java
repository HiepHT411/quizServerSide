package com.example.GoQuiz.controller;

import com.example.GoQuiz.config.SecurityConfig;
import com.example.GoQuiz.dto.AuthResponse;
import com.example.GoQuiz.dto.LoginRequest;
import com.example.GoQuiz.dto.SignupRequest;
import com.example.GoQuiz.exceptions.EmailAlreadyExistsException;
import com.example.GoQuiz.exceptions.PasswordTooWeakException;
import com.example.GoQuiz.exceptions.UsernameAlreadyExistsException;
import com.example.GoQuiz.model.User;
import com.example.GoQuiz.service.UserService;
import com.example.GoQuiz.util.JwtTokenProvider;
import com.example.GoQuiz.util.PasswordValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/authenticate")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authenticateAndGetToken(loginRequest.getEmail(), loginRequest.getPassword());
        return new AuthResponse(token);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.getUserByUsername(signupRequest.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(String.format("Username %s already been used", signupRequest.getUsername()));
        }
        if (userService.getUserByEmail(signupRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(String.format("Email %s already been used", signupRequest.getEmail()));
        }

        // validate password
        if (!PasswordValidator.check(signupRequest.getPassword())) {
            throw new PasswordTooWeakException("Password must be at least 8 characters long, contain at least one digit, one uppercase letter, one lowercase letter, and one special character");
        }

        userService.createUser(mapSignUpRequestToUser(signupRequest));

        String token = authenticateAndGetToken(signupRequest.getEmail(), signupRequest.getPassword());
        return new AuthResponse(token);
    }

    private String authenticateAndGetToken(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return jwtTokenProvider.generate(authentication);
    }

    private User mapSignUpRequestToUser(SignupRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setRole(SecurityConfig.USER);
        return user;
    }
}