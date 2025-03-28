package org.example.todoapp.services.impl;

import lombok.*;
import org.example.todoapp.exceptions.*;
import org.example.todoapp.models.*;
import org.example.todoapp.repositories.*;
import org.example.todoapp.services.ItemService;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item getItemById(Long id, Long userId) {
        var item = itemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Item not found"));

        if (item.getList().getUser().getId().equals(userId)) {
            return item;
        }
        throw new NotFoundException("Item not found");
    }

}
