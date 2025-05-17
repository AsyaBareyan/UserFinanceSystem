package org.example.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.EmailRequest;
import org.example.dto.request.EmailUpdateRequest;
import org.example.dto.response.EmailResponse;
import org.example.mapper.EmailMapper;
import org.example.service.AuthService;
import org.example.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailEndpoint {
    private final EmailService emailService;
    private final EmailMapper emailMapper;
    private final AuthService authService;

    @PostMapping
    public void addEmail(@RequestBody @Valid EmailRequest dto) {
        emailService.addEmail(authService.getCurrentUserId(), dto.email());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteEmail(@RequestBody @Valid EmailRequest dto) {
        emailService.removeEmail(authService.getCurrentUserId(), dto.email());
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public EmailResponse updateEmail(@RequestBody @Valid EmailUpdateRequest dto) {
        return emailMapper.toDto(emailService.replaceEmail(authService.getCurrentUserId(), dto.oldEmail(), dto.newEmail()));
    }

}
