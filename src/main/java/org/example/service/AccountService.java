package org.example.service;

import org.example.dto.response.AccountResponse;

import java.math.BigDecimal;

public interface AccountService {
    AccountResponse getAccountByUserId(Long userId);

    void transfer(Long fromUserId, Long toUserId, BigDecimal amount);
}

