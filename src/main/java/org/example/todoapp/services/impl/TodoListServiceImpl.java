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
        var exists = todoListRepository.existsByIdAndUserId(
            todoList.getId(),
            todoList.getUser().getId());
        if (exists) {
            throw new ConflictException("Todo list already exists");
        }
        return todoListRepository.save(todoList).getId();
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
        var existingTodoList = findByIdAndUserId(todoList.getId(), userId);
        updateTodoList(existingTodoList, todoList);
        todoListRepository.save(existingTodoList);
    }

    public void updateTodoList(TodoList existingList, TodoList newList) {
        existingList.setTitle(newList.getTitle());
        existingList.setDescription(newList.getDescription());
    }

    @Override
    public void addItem(Item item, Long userId) {
        item.setList(
            findByIdAndUserId(item.getList().getId(), userId));
        itemRepository.save(item);
    }

    @Override
    public List<Item> getItems(Long id, Long userId) {
        findByIdAndUserId(id, userId);
        return itemRepository.getItemsByListId(id);
    }

}
