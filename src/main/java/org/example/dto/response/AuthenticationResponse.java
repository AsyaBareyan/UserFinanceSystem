package org.example.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        Long id,
        String emailOrPhone,
        String name,
        String token) {
}
