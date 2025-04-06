package org.example.todoapp.services;

import java.util.*;

import org.example.todoapp.exceptions.*;
import org.example.todoapp.models.*;
import org.example.todoapp.repositories.*;
import org.example.todoapp.services.impl.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Test
    void getItemById_Success() {
        var todoList = new TodoList();
        todoList.setUser(User.builder().id(1L).build());
        var item = Item.builder()
            .id(1L).title("title")
            .description("description")
            .done(true).list(todoList)
            .build();

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        var result = itemService.getItemById(item.getId(), todoList.getUser().getId());
        Assertions.assertEquals(item, result);
    }

    @Test
    void getItemById_NotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemById(1L, 1L));
    }

    @Test
    void getItemById_WrongUser() {
        var todoList = new TodoList();
        todoList.setUser(User.builder().id(1L).build());
        var item = Item.builder()
            .id(1L).title("title")
            .description("description")
            .done(true).list(todoList)
            .build();

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemById(1L, 2L));
    }

    @Test
    void saveItem_Success() {
        var todoList = new TodoList();
        todoList.setUser(User.builder().id(1L).build());
        var item = Item.builder()
            .id(1L).title("title")
            .description("description")
            .done(true).list(todoList)
            .build();
        var updatedItem = Item.builder()
            .id(1L).title("updatedTitle")
            .description("updatedDescription")
            .done(true).list(todoList)
            .build();

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        itemService.saveItem(updatedItem, 1L);
        Assertions.assertEquals(updatedItem, item);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void saveItem_NotFound() {
        var item = Item.builder()
            .id(1L).build();
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> itemService.saveItem(item, 1L));
    }

    @Test
    void saveItem_WrongUser() {
        var todoList = new TodoList();
        todoList.setUser(User.builder().id(1L).build());
        var item = Item.builder()
            .id(1L).list(todoList).build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemById(1L, 2L));
    }

    @Test
    void deleteItem_Success() {
        Long itemId = 1L;
        Long userId = 1L;

        var user = User.builder().id(userId).build();
        var todoList = new TodoList();
        todoList.setUser(user);
        var item = Item.builder().id(itemId).list(todoList).build();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        itemService.deleteItem(itemId, userId);
        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    void deleteItem_NotFound() {
        Long itemId = 1L;
        Long userId = 1L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> itemService.deleteItem(itemId, userId));
        verify(itemRepository, never()).delete(any());
    }

    @Test
    void deleteItem_WrongUser() {
        Long itemId = 1L;
        Long correctUserId = 1L;
        Long wrongUserId = 2L;

        var user = User.builder().id(correctUserId).build();
        var todoList = new TodoList();
        todoList.setUser(user);
        var item = Item.builder().id(itemId).list(todoList).build();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Assertions.assertThrows(NotFoundException.class, () -> itemService.deleteItem(itemId, wrongUserId));
        verify(itemRepository, never()).delete(any());
    }
}
