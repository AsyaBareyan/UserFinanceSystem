package org.example.endpoint;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.AuthenticationRequest;
import org.example.dto.response.AuthenticationResponse;
import org.example.service.impl.AuthServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthEndpoint {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        return authService.authenticate(request);
    }
}
