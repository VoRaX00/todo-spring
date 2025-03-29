package org.example.todoapp.services;

import java.util.*;

import org.example.todoapp.models.*;

public interface TodoListService {
    Long save(TodoList todoList);
    List<TodoList> findByUserId(Long userId);
    TodoList findByIdAndUserId(Long id, Long userId);
}
