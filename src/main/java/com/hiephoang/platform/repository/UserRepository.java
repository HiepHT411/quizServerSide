package com.hiephoang.platform.repository;

import com.hiephoang.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByVerificationToken(String verificationToken);

    Optional<User> findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);
}