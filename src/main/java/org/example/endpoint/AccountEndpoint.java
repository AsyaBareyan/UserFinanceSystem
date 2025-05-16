package org.example.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.TransferRequest;
import org.example.dto.response.AccountResponse;
import org.example.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountEndpoint {
    private final AccountService accountService;

    @GetMapping()
    public AccountResponse getMyAccount(Authentication authentication) {
        var userId = (Long) authentication.getPrincipal();
        return accountService.getAccountByUserId(userId);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody @Valid TransferRequest dto, Authentication authentication) {
        var fromUserId = (Long) authentication.getPrincipal();
        accountService.transfer(fromUserId, dto.toUserId(), dto.amount());
    }
}
