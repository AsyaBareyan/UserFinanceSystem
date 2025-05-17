package org.example.dto.request;

import java.time.LocalDate;

public record UserFilterRequest(
        String name,
        String email,
        String phone,
        LocalDate dateOfBirth
) {
}
