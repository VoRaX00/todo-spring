package org.example.todoapp.controllers;

import io.swagger.v3.oas.annotations.security.*;
import lombok.*;
import lombok.extern.slf4j.*;
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
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/items")
@Slf4j
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
    public ResponseEntity<?> getItem(@PathVariable Long id) {
        var user = getAuthenticatedUser();
        var item = itemService.getItemById(id, user.getId());
        return ResponseEntity.ok(itemMapper.toItemGetDto(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(
        @PathVariable Long id,
        @RequestBody ItemUpdateDto itemUpdateDto
    ) {
        var user = getAuthenticatedUser();
        var item = itemMapper.toModel(itemUpdateDto);
        item.setId(id);
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
