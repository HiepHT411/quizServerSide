package com.hiephoang.platform.service;

import com.hiephoang.platform.config.CommonResource;
import com.hiephoang.platform.exceptions.UserNotFoundException;
import com.hiephoang.platform.model.User;
import com.hiephoang.platform.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CommonResource commonResource;

    private final JavaMailSender mailSender;

    private final UserRepository repository;

    private final SpringTemplateEngine templateEngine;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Override
    public User createUser(User user) {
        // Generate verification token and send email
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        String activateURL = String.format("%s:%s/api/auth/verify-email?token=%s", commonResource.getServerHost(), commonResource.getServerPort(), verificationToken);
        Map<String, Object> attributes = new HashMap<>() {{
            put("username", user.getUsername());
            put("activateLink", activateURL);
        }};

        executorService.execute(() -> {
            try {
                sendMail(user.getEmail(), new File("src/main/resources/static/welcome.png"), attributes);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });

        return repository.save(user);
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) throws UserNotFoundException {
        return repository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
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

    private void sendMail(String mailAddress, File attachment, Map<String, Object> attributes) throws MessagingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(attributes);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(mailAddress);
        helper.setSubject("Registration Letter");
        String body = templateEngine.process("registration-letter-template", thymeleafContext);
        helper.setText(body, true);
        helper.addAttachment(attachment.getName(), attachment);

        mailSender.send(message);
        log.info("Send mail registration to {} successfully", mailAddress);
    }

    public boolean validateVerificationToken(String token) {
        User user = repository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return false;
        }

        user.setEnabled(true);
        repository.save(user);
        return true;
    }
}
