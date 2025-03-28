package org.example.todoapp.services;

import org.example.todoapp.models.*;

public interface ItemService {
    Item getItemById(Long id, Long userId);
}
