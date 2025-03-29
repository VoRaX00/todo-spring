package org.example.todoapp.controllers;

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
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    private UserDetailsImpl getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            throw new UnauthorizedException("not authenticated");
        }
        return userDetails;
    }

    @GetMapping("/{id}")
    public ItemGetDto getItem(@PathVariable Long id) {
        var user = getAuthenticatedUser();
        var item = itemService.getItemById(id, user.getId());
        return itemMapper.toItemGetDto(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody Item item) {
        var user = getAuthenticatedUser();
        itemService.saveItem(item, user.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        var user = getAuthenticatedUser();
        itemService.deleteItem(id, user.getId());
        return ResponseEntity.ok().build();
    }

}
