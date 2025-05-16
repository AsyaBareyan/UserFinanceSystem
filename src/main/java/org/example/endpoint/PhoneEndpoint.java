package org.example.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.PhoneRequest;
import org.example.dto.request.PhoneUpdateRequest;
import org.example.dto.response.PhoneResponse;
import org.example.mapper.PhoneMapper;
import org.example.service.AuthService;
import org.example.service.PhoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phones")
@RequiredArgsConstructor
public class PhoneEndpoint {
    private final PhoneService phoneService;
    private final PhoneMapper phoneMapper;
    private final AuthService authService;

    @PostMapping
    public void addPhone(@RequestBody @Valid PhoneRequest dto) {
        phoneService.addPhone(authService.getCurrentUserId(), dto.phone());
    }

    @DeleteMapping
    public ResponseEntity<?> deletePhone(@RequestBody @Valid PhoneRequest dto) {
        phoneService.removePhone(authService.getCurrentUserId(), dto.phone());
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public PhoneResponse updatePhone(@RequestBody @Valid PhoneUpdateRequest dto) {
        return phoneMapper.toDto(phoneService.replacePhone(authService.getCurrentUserId(), dto.oldPhone(), dto.newPhone()));
    }
}
