package org.example.todoapp.mappers;

import org.example.todoapp.dto.*;
import org.example.todoapp.models.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TodoListMapper {

    TodoList toModel(TodoListCreateDto listCreateDto);

    TodoList toModel(TodoListUpdateDto listUpdateDto);

    TodoListGetDto toGetDto(TodoList todoList);

}
