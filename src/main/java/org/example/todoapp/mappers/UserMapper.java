package org.example.todoapp.mappers;

import org.example.todoapp.dto.UserRegisterDto;
import org.example.todoapp.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toModel(UserRegisterDto user);
}
