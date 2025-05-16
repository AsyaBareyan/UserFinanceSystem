package org.example.mapper;

import org.example.dto.response.AccountResponse;
import org.example.model.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toDto(Account account);
}
