package org.example.todoapp.services;

import org.example.todoapp.models.*;

public interface ItemService {
    Item getItemById(Long id, Long userId);
    void saveItem(Item item, Long userId);
    void deleteItem(Long id, Long userId);
}
