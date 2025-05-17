package org.example.service;

import org.example.model.entity.EmailData;

public interface EmailService {
    void addEmail(Long userId, String email);

    void removeEmail(Long userId, String email);

    EmailData replaceEmail(Long userId, String oldEmail, String newEmail);
}
