package org.example.todoapp.services;

import java.util.*;

import org.example.todoapp.models.*;

public interface TodoListService {
    Long save(TodoList todoList);
    List<TodoList> findByUserId(Long userId);
    TodoList findByIdAndUserId(Long id, Long userId);
    void update(TodoList todoList, Long userId);
    void addItem(Item item, Long userId);
    List<Item> getItems(Long id, Long userId);
}
