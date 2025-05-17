package org.example.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.AccountResponse;
import org.example.exception.AccountNotFoundException;
import org.example.exception.SelfTransferException;
import org.example.mapper.AccountMapper;
import org.example.model.entity.Account;
import org.example.repository.AccountRepository;
import org.example.service.AccountService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponse getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .map(accountMapper::toDto)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for user id " + userId));
    }

    @Scheduled(fixedRate = 30_000)
    @Transactional
    public void increaseBalances() {
        log.info("Running scheduled balance increase");
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            var maxBalance = account.getInitialBalance().multiply(BigDecimal.valueOf(2.07));
            var current = account.getBalance();
            if (current.compareTo(maxBalance) < 0) {
                var increased = current.multiply(BigDecimal.valueOf(1.10));
                if (increased.compareTo(maxBalance) > 0) {
                    increased = maxBalance;
                }
                log.info("Updating balance for user {} from {} to {}",
                        account.getUser().getId(), current, increased);
                account.setBalance(increased);
            }
        }
    }


    @Override
    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (fromUserId.equals(toUserId)) {
            throw new SelfTransferException("Cannot transfer to yourself");
        }

        Account from = accountRepository.findByUserIdForUpdate(fromUserId)
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        Account to = accountRepository.findByUserIdForUpdate(toUserId)
                .orElseThrow(() -> new AccountNotFoundException("Recipient account not found"));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
    }
}
