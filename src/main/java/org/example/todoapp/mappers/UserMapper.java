package org.example.todoapp.mappers;

import org.example.todoapp.dto.UserRegisterDto;
import org.example.todoapp.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toModel(UserRegisterDto user);
}
