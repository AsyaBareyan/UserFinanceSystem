package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PhoneUpdateRequest(
        @NotBlank
        String oldPhone,
        @NotBlank
        String newPhone) {
}
