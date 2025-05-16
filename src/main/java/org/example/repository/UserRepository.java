package org.example.repository;

import org.example.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("SELECT e.user FROM EmailData e WHERE e.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT p.user FROM PhoneData p WHERE p.phone = :phone")
    Optional<User> findByPhone(String phone);

}
