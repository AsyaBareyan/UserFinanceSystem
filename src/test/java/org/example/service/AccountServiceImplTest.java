package org.example.service;

import org.example.dto.response.AccountResponse;
import org.example.exception.AccountNotFoundException;
import org.example.exception.SelfTransferException;
import org.example.mapper.AccountMapper;
import org.example.model.entity.Account;
import org.example.repository.AccountRepository;
import org.example.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void getAccountByUserId_shouldReturnDto() {
        Long userId = 1L;
        Account account = new Account();
        AccountResponse dto = new AccountResponse(userId ,account.getBalance(),account.getInitialBalance());

        when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(dto);

        AccountResponse result = accountService.getAccountByUserId(userId);

        assertEquals(dto, result);
    }

    @Test
    void getAccountByUserId_notFound_shouldThrow() {
        when(accountRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByUserId(1L));
    }

    @Test
    void transfer_sameUser_shouldThrow() {
        Long userId = 1L;
        assertThrows(SelfTransferException.class, () -> accountService.transfer(userId, userId, BigDecimal.TEN));
    }

    @Test
    void transfer_insufficientBalance_shouldThrow() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100);

        Account from = new Account();
        from.setBalance(BigDecimal.valueOf(50));
        Account to = new Account();
        to.setBalance(BigDecimal.ZERO);

        when(accountRepository.findByUserIdForUpdate(fromUserId)).thenReturn(Optional.of(from));
        when(accountRepository.findByUserIdForUpdate(toUserId)).thenReturn(Optional.of(to));

        assertThrows(IllegalStateException.class, () -> accountService.transfer(fromUserId, toUserId, amount));
    }

    @Test
    void transfer_successful() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = BigDecimal.valueOf(50);

        Account from = new Account();
        from.setBalance(BigDecimal.valueOf(100));
        Account to = new Account();
        to.setBalance(BigDecimal.valueOf(20));

        when(accountRepository.findByUserIdForUpdate(fromUserId)).thenReturn(Optional.of(from));
        when(accountRepository.findByUserIdForUpdate(toUserId)).thenReturn(Optional.of(to));

        accountService.transfer(fromUserId, toUserId, amount);

        assertEquals(BigDecimal.valueOf(50), from.getBalance());
        assertEquals(BigDecimal.valueOf(70), to.getBalance());
    }
}
