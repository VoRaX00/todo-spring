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

    @Override
    public void saveItem(Item item, Long userId) {
        var found = itemRepository.findById(item.getId())
            .orElseThrow(() -> new NotFoundException("Item not found"));
        if (!found.getList().getUser().getId().equals(userId)) {
            throw new NotFoundException("Item not found");
        }

        found.setTitle(item.getTitle());
        found.setDescription(item.getDescription());
        found.setDone(item.getDone());
        itemRepository.save(found);
    }

    @Override
    public void deleteItem(Long id, Long userId) {
        var found = itemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Item not found"));
        if (!found.getList().getUser().getId().equals(userId)) {
            throw new NotFoundException("Item not found");
        }
        itemRepository.delete(found);
    }

}
