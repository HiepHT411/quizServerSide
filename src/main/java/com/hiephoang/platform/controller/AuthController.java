package com.hiephoang.platform.controller;

import com.hiephoang.platform.config.SecurityConfig;
import com.hiephoang.platform.dto.*;
import com.hiephoang.platform.exceptions.EmailAlreadyExistsException;
import com.hiephoang.platform.exceptions.PasswordTooWeakException;
import com.hiephoang.platform.exceptions.UsernameAlreadyExistsException;
import com.hiephoang.platform.model.User;
import com.hiephoang.platform.service.UserService;
import com.hiephoang.platform.util.JwtTokenProvider;
import com.hiephoang.platform.util.PasswordValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<ResponseMetaData> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> userOpt = userService.getUserByEmail(loginRequest.getEmail());
            if (userOpt.isEmpty()) {
                userOpt = userService.getUserByUsername(loginRequest.getEmail());
                if (userOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.NOT_FOUND), String.format("User with email/username %s is not found", loginRequest.getEmail())));
                }
            }
            if (!userOpt.get().isEnabled())
                return ResponseEntity.status(MetaData.INACTIVE_ACCOUNT.getMetaCode()).body(new ResponseMetaData(new MetaDTO(MetaData.INACTIVE_ACCOUNT), String.format("User with email/username %s is disabled", loginRequest.getEmail())));

            String token = authenticateAndGetToken(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), new AuthResponse(token)));
        } catch (Exception e) {
            return ResponseEntity.status(MetaData.UNAUTHORIZED.getMetaCode()).body(new ResponseMetaData(new MetaDTO(MetaData.UNAUTHORIZED), null));
        }
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