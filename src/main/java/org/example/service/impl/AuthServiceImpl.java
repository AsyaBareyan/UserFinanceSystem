package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.AuthenticationRequest;
import org.example.dto.response.AuthenticationResponse;
import org.example.exception.InvalidAuthenticationCredentialsException;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.example.util.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var login = request.emailOrPhone();
        var password = request.password();
        log.info("Attempting login with: {}", login);
        var optionalUser = isEmail(login)
                ? userRepository.findByEmail(login)
                : userRepository.findByPhone(login);

        var user = optionalUser.orElseThrow(() -> {
            log.warn("User not found for {}", login);
            return new UsernameNotFoundException("User not found");
        });
        log.info("Found user: {}", user.getName());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Invalid password for {}", login);
            throw new InvalidAuthenticationCredentialsException("Provided wrong credentials for authentication");
        }

        var token = jwtUtil.generateToken(user.getId());
        log.info("Generated token for userId {}: {}", user.getId(), token);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .token(token)
                .emailOrPhone(request.emailOrPhone())
                .build();
    }

    private boolean isEmail(String input) {
        return input != null && input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }


    @Override
    public Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
