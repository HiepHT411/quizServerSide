package com.hiephoang.platform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DeactivatedUserException extends RuntimeException {
    public DeactivatedUserException(String message) {
        super(message);
    }
}
