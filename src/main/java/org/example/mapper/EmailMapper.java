package org.example.mapper;

import org.example.dto.response.EmailResponse;
import org.example.model.entity.EmailData;
import org.example.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    @Mapping(target = "id", ignore = true)
    EmailData toEntity(String email, User user);

    EmailResponse toDto(EmailData emailData);

}
