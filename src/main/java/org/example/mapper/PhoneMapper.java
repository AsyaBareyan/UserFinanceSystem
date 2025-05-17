package org.example.mapper;

import org.example.dto.response.PhoneResponse;
import org.example.model.entity.PhoneData;
import org.example.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhoneMapper {
    @Mapping(target = "id", ignore = true)
    PhoneData toEntity(String phone, User user);

    PhoneResponse toDto(PhoneData phoneData);
}
