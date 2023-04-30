package com.example.GoQuiz.dto;

import com.example.GoQuiz.model.User;
import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String email;
    private String password;

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
