package org.example.todoapp.controllers;

import java.util.*;

import lombok.*;
import org.example.todoapp.models.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class TodoListController {

    @PostMapping
    public Long createList(){
        return null;
    }

    @GetMapping
    public List<TodoList> getTodoLists(){
        return null;
    }

    @GetMapping("/{id}")
    public TodoList getById(@PathVariable Long id) {
        return null;
    }

    @PutMapping("/{id}")
    public void updateList(@PathVariable Long id) {

    }

    @PostMapping("/{id}/items")
    public void addItem(@PathVariable Long id) {

    }

    @GetMapping("/{id}/items")
    public List<Item> getItemsByList(@PathVariable Long id) {
        return null;
    }
}
