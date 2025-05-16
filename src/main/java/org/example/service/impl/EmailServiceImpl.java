package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mapper.EmailMapper;
import org.example.model.entity.EmailData;
import org.example.repository.EmailDataRepository;
import org.example.repository.UserRepository;
import org.example.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailDataRepository emailRepository;
    private final UserRepository userRepository;
    private final EmailMapper emailMapper;

    @Override
    public void addEmail(Long userId, String email) {
        if (emailRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use");
        }
        var user = userRepository.findById(userId).orElseThrow();
        var emailData = emailMapper.toEntity(email, user);
        emailRepository.save(emailData);
    }

    @Override
    public void removeEmail(Long userId, String email) {
        var user = userRepository.findById(userId).orElseThrow();

        List<EmailData> userEmails = user.getEmails();
        if (userEmails.size() <= 1) {
            throw new IllegalStateException("Cannot delete the last remaining email");
        }

        var emailData = emailRepository.findByEmail(email)
                .filter(e -> e.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Email not found or not owned by user"));

        emailRepository.delete(emailData);
    }

    @Override
    public EmailData replaceEmail(Long userId, String oldEmail, String newEmail) {
        if (emailRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("New email is already in use");
        }
        var emailData = emailRepository.findByEmail(oldEmail)
                .filter(e -> e.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Old email not found or not owned by user"));
        emailData.setEmail(newEmail);
        return emailRepository.save(emailData);
    }
}
