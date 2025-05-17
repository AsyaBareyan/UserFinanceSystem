package org.example.dto.response;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String name,
        LocalDate dateOfBirth,
        String email,
        String phone
) {
}
