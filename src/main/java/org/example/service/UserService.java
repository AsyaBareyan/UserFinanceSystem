package org.example.service;

import org.example.dto.request.UserFilterRequest;
import org.example.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> filterUsers(UserFilterRequest filter, Pageable pageable);
}
