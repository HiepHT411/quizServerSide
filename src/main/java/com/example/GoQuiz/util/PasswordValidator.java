package com.example.GoQuiz.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    public static boolean check(String password) {

        if (password.length() >= 8) {
            Pattern upperCase = Pattern.compile("[A-Z]");
            Pattern lowerCase = Pattern.compile("[a-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasUpper = upperCase.matcher(password);
            Matcher hasLower = lowerCase.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasUpper.find() && hasLower.find() && hasDigit.find() && hasSpecial.find();

        } else
            return false;

    }
}
