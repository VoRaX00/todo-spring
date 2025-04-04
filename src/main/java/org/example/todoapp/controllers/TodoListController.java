package org.example.todoapp.controllers;

import java.util.*;

import io.swagger.v3.oas.annotations.security.*;
import lombok.*;
import org.example.todoapp.dto.*;
import org.example.todoapp.exceptions.*;
import org.example.todoapp.mappers.*;
import org.example.todoapp.models.*;
import org.example.todoapp.services.*;
import org.springframework.http.*;
import org.springframework.security.core.context.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists")
@SecurityRequirement(name = "BearerAuth")
@RequiredArgsConstructor
public class TodoListController {

    private final TodoListService todoListService;

    private final TodoListMapper todoListMapper;

    private final ItemMapper itemMapper;

    private UserDetailsImpl getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            throw new UnauthorizedException("not authenticated");
        }
        return userDetails;
    }

    @PostMapping
    public ResponseEntity<?> createList(@RequestBody TodoListCreateDto listCreateDto) {
        var userDetails = getAuthenticatedUser();
        var todoList = todoListMapper.toModel(listCreateDto);
        todoList.setUser(User.builder().id(userDetails.getId()).build());
        var todoListId = todoListService.save(todoList);
        return new ResponseEntity<>(todoListId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> findByUserId() {
        var userDetails = getAuthenticatedUser();
        var lists = todoListService.findByUserId(userDetails.getId());
        var result =  lists.stream()
            .map(todoListMapper::toGetDto)
            .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        var userDetails = getAuthenticatedUser();
        var list = todoListService.findByIdAndUserId(id, userDetails.getId());
        return new ResponseEntity<>(todoListMapper.toGetDto(list), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateList(
        @PathVariable Long id,
        @RequestBody TodoListUpdateDto todoListUpdateDto
    ) {
        var userDetails = getAuthenticatedUser();
        var todoList = todoListMapper.toModel(todoListUpdateDto);
        todoList.setId(id);
        todoListService.update(todoList, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<?> addItem(
        @PathVariable Long id,
        @RequestBody ItemCreateDto itemCreateDto
    ) {
        var userDetails = getAuthenticatedUser();
        var item = itemMapper.toModel(itemCreateDto);
        item.setList(TodoList.builder()
            .id(id).build());
        todoListService.addItem(item, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}/items")
    public List<ItemGetDto> getItemsByList(@PathVariable Long id) {
        var userDetails = getAuthenticatedUser();
        var items = todoListService.getItems(id, userDetails.getId());
        return items.stream()
            .map(itemMapper::toItemGetDto)
            .toList();
    }

}
