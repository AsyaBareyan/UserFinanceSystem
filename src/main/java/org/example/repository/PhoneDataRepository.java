package org.example.repository;

import org.example.model.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
    boolean existsByPhone(String phone);

    Optional<PhoneData> findByPhone(String phone);
}
