package com.example.GoQuiz.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Email(message = "Email not valid")
    private String email;
    @NotBlank
    private String password;
}
