package org.example.dto.request;

public record AuthenticationRequest(
        String emailOrPhone,
        String password
) {
}
