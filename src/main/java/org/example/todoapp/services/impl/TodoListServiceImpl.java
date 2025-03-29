package org.example.todoapp.services.impl;

import java.util.*;

import lombok.*;
import org.example.todoapp.exceptions.*;
import org.example.todoapp.exceptions.BadRequestException;
import org.example.todoapp.models.*;
import org.example.todoapp.repositories.*;
import org.example.todoapp.services.TodoListService;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class TodoListServiceImpl implements TodoListService {
    private final TodoListRepository todoListRepository;

    @Override
    public Long save(TodoList todoList) {
        var exists = todoListRepository.findTodoListByIdAndUserId(todoList.getId(),
            todoList.getUser().getId());

        if (exists.isEmpty()) {
            return todoListRepository
                .save(todoList)
                .getId();
        }
        throw new ConflictException("Todo list already exists");
    }

    @Override
    public List<TodoList> findByUserId(Long userId) {
        return todoListRepository.findByUserId(userId);
    }

    @Override
    public TodoList findByIdAndUserId(Long id, Long userId) {
        return todoListRepository.findTodoListByIdAndUserId(id, userId)
            .orElseThrow(() -> new NotFoundException("Not found todo list"));
    }

    @Override
    public void update(TodoList todoList, Long userId) {
        var existingTodoList = todoListRepository.findById(todoList.getId())
            .orElseThrow(() -> new NotFoundException("Todo list not found"));
        if (!existingTodoList.getUser().getId().equals(userId)) {
            throw new BadRequestException("User id mismatch");
        }

        existingTodoList.setTitle(todoList.getTitle());
        existingTodoList.setDescription(todoList.getDescription());
        todoListRepository.save(existingTodoList);
    }

}
