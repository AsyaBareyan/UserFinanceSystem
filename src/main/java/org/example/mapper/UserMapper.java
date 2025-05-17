package org.example.mapper;

import org.example.dto.response.UserResponse;
import org.example.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse mapToDto(User user);
}
