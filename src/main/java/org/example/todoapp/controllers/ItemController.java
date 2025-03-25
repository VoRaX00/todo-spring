package org.example.todoapp.controllers;

import org.example.todoapp.models.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {
    @GetMapping("/{id}")
    public Item getItem(@PathVariable Long id) {
        return null;
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {

    }
}
