package org.example.todoapp.mappers;

import org.example.todoapp.dto.*;
import org.example.todoapp.models.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemGetDto toItemGetDto(Item item);
    Item toModel(ItemCreateDto itemCreateDto);
}
