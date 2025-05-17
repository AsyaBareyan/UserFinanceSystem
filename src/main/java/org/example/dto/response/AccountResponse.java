package org.example.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        BigDecimal balance,
        BigDecimal initialBalance
) {}
