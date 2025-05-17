package org.example.repository;

import org.example.model.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    boolean existsByEmail(String email);

    Optional<EmailData> findByEmail(String email);
}
