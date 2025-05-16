package org.example.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailUpdateRequest(
        @NotBlank
        @Email
        String oldEmail,
        @NotBlank
        @Email String newEmail) {
}
