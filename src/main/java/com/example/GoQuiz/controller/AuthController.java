package com.example.GoQuiz.controller;

import com.example.GoQuiz.config.SecurityConfig;
import com.example.GoQuiz.dto.AuthResponse;
import com.example.GoQuiz.dto.LoginRequest;
import com.example.GoQuiz.dto.SignupRequest;
import com.example.GoQuiz.exceptions.*;
import com.example.GoQuiz.model.User;
import com.example.GoQuiz.service.UserService;
import com.example.GoQuiz.util.JwtTokenProvider;
import com.example.GoQuiz.util.PasswordValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
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
        Optional<User> userOpt = userService.getUserByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException(String.format("User with email %s is not found", loginRequest.getEmail()));
        }
        if (!userOpt.get().isEnabled())
            throw new DeactivatedUserException(String.format("User with email %s is disabled", loginRequest.getEmail()));
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

//        String token = authenticateAndGetToken(signupRequest.getEmail(), signupRequest.getPassword());
//        return new AuthResponse(token);
        return new AuthResponse("Signed up successfully, please activate your account by activate link sent in your mail box.");
    }

    @GetMapping("/verify-email")
    public AuthResponse verifyEmail(@RequestParam("token") String token) {
        boolean result = userService.validateVerificationToken(token);
        if (result) {
            return new AuthResponse("Account has been verified successfully.");
        } else {
            return new AuthResponse("Invalid verification token.");
        }
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