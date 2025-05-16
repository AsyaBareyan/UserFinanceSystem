package org.example.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.UserFilterRequest;
import org.example.model.entity.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static org.example.specification.UserSpecification.dateOfBirthAfter;
import static org.example.specification.UserSpecification.emailEquals;
import static org.example.specification.UserSpecification.nameLike;
import static org.example.specification.UserSpecification.phoneEquals;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Page<User> filterUsers(UserFilterRequest filter, Pageable pageable) {
        var spec = Specification.where(nameLike(filter.name()))
                .and(emailEquals(filter.email()))
                .and(phoneEquals(filter.phone()))
                .and(dateOfBirthAfter(filter.dateOfBirth()));
        return userRepository.findAll(spec, pageable);
    }

}
