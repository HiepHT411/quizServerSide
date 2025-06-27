package com.hiephoang.platform.service;

import com.hiephoang.platform.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);

    List<User> getAllUsers();

    void deleteUser(User user);

    boolean validateVerificationToken(String token);
}

