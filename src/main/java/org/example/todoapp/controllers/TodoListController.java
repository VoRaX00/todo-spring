package org.example.todoapp.controllers;

import java.util.*;

import lombok.*;
import org.example.todoapp.dto.*;
import org.example.todoapp.exceptions.*;
import org.example.todoapp.mappers.*;
import org.example.todoapp.models.*;
import org.example.todoapp.services.*;
import org.springframework.security.core.context.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class TodoListController {
    private final TodoListService listService;

    private final TodoListMapper todoListMapper;

    private UserDetailsImpl getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            throw new UnauthorizedException("not authenticated");
        }
        return userDetails;
    }

    @PostMapping
    public Long createList(@RequestBody TodoListCreateDto listCreateDto){
        var userDetails = getAuthenticatedUser();
        var todoList = todoListMapper.toModel(listCreateDto);
        todoList.setUser(User.builder().id(userDetails.getId()).build());
        return listService.save(todoList);
    }

    @GetMapping
    public List<TodoListGetDto> findByUserId(){
        var userDetails = getAuthenticatedUser();
        var lists = listService.findByUserId(userDetails.getId());
        return lists.stream()
            .map(todoListMapper::toGetDto)
            .toList();
    }

    @GetMapping("/{id}")
    public TodoListGetDto findById(@PathVariable Long id) {
        var userDetails = getAuthenticatedUser();
        var list = listService.findByIdAndUserId(id, userDetails.getId());
        return todoListMapper.toGetDto(list);
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
