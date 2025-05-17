package org.example.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull Long toUserId,
        @NotNull
        @DecimalMin("10")
        BigDecimal amount
) {
}
