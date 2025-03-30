package org.example.todoapp.services.impl;

import java.util.*;

import lombok.*;
import org.example.todoapp.exceptions.*;
import org.example.todoapp.models.*;
import org.example.todoapp.repositories.*;
import org.example.todoapp.services.TodoListService;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class TodoListServiceImpl implements TodoListService {

    private final TodoListRepository todoListRepository;

    private final ItemRepository itemRepository;

    @Override
    public Long save(TodoList todoList) {
        var exists = todoListRepository.findByIdAndUserId(
            todoList.getId(),
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
        return todoListRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new NotFoundException("Not found todo list"));
    }

    @Override
    public void update(TodoList todoList, Long userId) {
        var existingTodoList = todoListRepository.findByIdAndUserId(todoList.getId(), userId)
            .orElseThrow(() -> new NotFoundException("Todo list not found"));

        existingTodoList.setTitle(todoList.getTitle());
        existingTodoList.setDescription(todoList.getDescription());
        todoListRepository.save(existingTodoList);
    }

    @Override
    public void addItem(Item item, Long userId) {
        var existingTodoList = todoListRepository.findByIdAndUserId(item.getList().getId(), userId)
            .orElseThrow(() -> new NotFoundException("Todo list not found"));
        item.setList(existingTodoList);
        itemRepository.save(item);
    }

}
