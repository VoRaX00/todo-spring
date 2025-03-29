package org.example.todoapp.services.impl;

import lombok.*;
import org.example.todoapp.models.*;
import org.example.todoapp.repositories.*;
import org.example.todoapp.services.TodoListService;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class TodoListServiceImpl implements TodoListService {
    private TodoListRepository todoListRepository;

    @Override
    public Long save(TodoList todoList) {
        return todoListRepository
            .save(todoList)
            .getId();
    }

}
