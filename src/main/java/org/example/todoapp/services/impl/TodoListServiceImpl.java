package org.example.todoapp.services.impl;

import java.util.*;

import lombok.*;
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
        return todoListRepository
            .save(todoList)
            .getId();
    }

    @Override
    public List<TodoList> findByUserId(Long userId) {
        return todoListRepository.findByUserId(userId);
    }

}
