package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class InvalidAuthenticationCredentialsException extends RuntimeException {
    public InvalidAuthenticationCredentialsException(String message) {
        super(message);
    }
}
