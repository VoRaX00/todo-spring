package org.example.todoapp.controllers;

import lombok.*;
import org.example.todoapp.dto.*;
import org.example.todoapp.exceptions.*;
import org.example.todoapp.mappers.*;
import org.example.todoapp.models.*;
import org.example.todoapp.services.*;
import org.springframework.security.core.context.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    private final UserService userService;

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
        var email = user.getEmail();
        var userId = userService.findUserIdByEmail(email);
        var item = itemService.getItemById(id, userId);

        return itemMapper.toItemGetDto(item);
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {

    }

}
