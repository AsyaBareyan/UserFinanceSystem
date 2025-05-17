package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PhoneRequest(
        @NotBlank
        String phone) {
}
