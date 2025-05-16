package org.example.endpoint;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.UserFilterRequest;
import org.example.dto.response.UserResponse;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserEndpoint {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/filter")
    public Page<UserResponse> filterProducts(@RequestBody UserFilterRequest filter,
                                             @PageableDefault Pageable pageable) {
        var usersPage = userService.filterUsers(filter, pageable);
        return usersPage.map(userMapper::mapToDto);
    }

}
