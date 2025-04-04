package org.example.todoapp.controllers;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.example.todoapp.dto.*;
import org.example.todoapp.exceptions.*;
import org.example.todoapp.mappers.*;
import org.example.todoapp.models.*;
import org.example.todoapp.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoListControllerTest {

    @MockBean
    private TodoListService todoListService;

    @MockBean
    private UserService userService;

    @MockBean
    private TodoListMapper todoListMapper;

    @MockBean
    private ItemMapper itemMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final long todoListId = 5L;

    private final long userId = 1L;

    private TodoList todoList;

    private TodoListCreateDto todoListCreateDto;

    private TodoListGetDto todoListGetDto;

    private TodoListUpdateDto todoListUpdateDto;

    private TodoList todoListUpdated;

    private Item item;

    private ItemCreateDto itemCreateDto;

    private ItemGetDto itemGetDto;

    @BeforeEach
    void setUp() {
        todoList = TodoList.builder()
            .id(todoListId)
            .title("todo-list-title")
            .description("todo-list-description")
            .build();

        todoListCreateDto = TodoListCreateDto.builder()
            .title("todo-list-title")
            .description("todo-list-description")
            .build();

        todoListGetDto = TodoListGetDto.builder()
            .id(todoListId)
            .title("todo-list-title")
            .description("todo-list-description")
            .build();

        todoListUpdateDto = TodoListUpdateDto.builder()
            .title("todo-list-title-upd")
            .description("todo-list-description-upd")
            .build();

        todoListUpdated = TodoList.builder()
            .id(todoListId)
            .title("todo-list-title-upd")
            .description("todo-list-description-upd")
            .build();

        item = Item.builder()
            .id(1L)
            .title("item-title")
            .description("item-description")
            .done(false)
            .build();

        itemCreateDto = ItemCreateDto.builder()
            .title("item-title")
            .description("item-description")
            .done(false)
            .build();

        itemGetDto = ItemGetDto.builder()
            .id(item.getId())
            .title("item-title")
            .description("item-description")
            .done(false)
            .build();
    }

    @Test
    void createList_Success() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
        setUserDetailForAuthentication(userDetails);

        when(todoListMapper.toModel(todoListCreateDto)).thenReturn(todoList);
        when(todoListService.save(todoList)).thenReturn(todoListId);

        mockMvc.perform(MockMvcRequestBuilders.post("/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoListCreateDto)))
            .andExpect(status().isCreated());
    }

    @Test
    void createList_Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoListCreateDto)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void findByUserId_Success() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
        setUserDetailForAuthentication(userDetails);

        when(todoListService.findByUserId(userId)).thenReturn(List.of(todoList));
        when(todoListMapper.toGetDto(todoList)).thenReturn(todoListGetDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/lists"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(todoListGetDto.getId()))
            .andExpect(jsonPath("$[0].title").value(todoListGetDto.getTitle()))
            .andExpect(jsonPath("$[0].description").value(todoListGetDto.getDescription()));
    }

    @Test
    void findByUserId_Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lists"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void findById_Success() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
        setUserDetailForAuthentication(userDetails);

        when(todoListService.findByIdAndUserId(todoListId, userId)).thenReturn(todoList);
        when(todoListMapper.toGetDto(todoList)).thenReturn(todoListGetDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/lists/{id}", todoListId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(todoListGetDto.getId()))
            .andExpect(jsonPath("$.title").value(todoListGetDto.getTitle()))
            .andExpect(jsonPath("$.description").value(todoListGetDto.getDescription()));
    }

    @Test
    void findById_Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lists/{id}", todoListId))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void findById_NotFound() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
        setUserDetailForAuthentication(userDetails);

        when(todoListService.findByIdAndUserId(todoListId, userId)).thenThrow(new NotFoundException("Not found"));
        mockMvc.perform(MockMvcRequestBuilders.get("/lists/{id}", todoListId))
            .andExpect(status().isNotFound());
    }

    @Test
    void updateList_Success() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
        setUserDetailForAuthentication(userDetails);

        when(todoListMapper.toModel(todoListUpdateDto)).thenReturn(todoListUpdated);
        doNothing().when(todoListService).update(todoListUpdated, userId);
        mockMvc.perform(MockMvcRequestBuilders.put("/lists/{id}", todoListId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(todoListUpdateDto)))
            .andExpect(status().isOk());
    }

    @Test
    void updateList_Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/lists/{id}", todoListId))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void updateList_NotFound() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
        setUserDetailForAuthentication(userDetails);

        when(todoListMapper.toModel(todoListUpdateDto)).thenReturn(todoListUpdated);
        doThrow(new NotFoundException("Not found")).when(todoListService).update(todoListUpdated, userId);
        mockMvc.perform(MockMvcRequestBuilders.put("/lists/{id}", todoListId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoListUpdateDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    void addItem_Success() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
        setUserDetailForAuthentication(userDetails);

        when(itemMapper.toModel(itemCreateDto)).thenReturn(item);
        doNothing().when(todoListService).addItem(item, userId);
        mockMvc.perform(MockMvcRequestBuilders.post("/lists/{id}/items", todoListId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(itemCreateDto)))
            .andExpect(status().isCreated());
    }

    @Test
    void addItem_Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/lists/{id}/items", todoListId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(itemCreateDto)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void addItem_NotFound() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
        setUserDetailForAuthentication(userDetails);

        when(itemMapper.toModel(itemCreateDto)).thenReturn(item);
        doThrow(new NotFoundException("Not found")).when(todoListService).addItem(item, userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/lists/{id}/items", todoListId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(itemCreateDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    void getItemsByList_Success() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
        setUserDetailForAuthentication(userDetails);

        when(todoListService.getItems(todoListId, userId)).thenReturn(List.of(item));
        when(itemMapper.toItemGetDto(item)).thenReturn(itemGetDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/lists/{id}/items", todoListId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(item.getId()))
            .andExpect(jsonPath("$[0].title").value(item.getTitle()))
            .andExpect(jsonPath("$[0].description").value(item.getDescription()))
            .andExpect(jsonPath("$[0].done").value(item.getDone()));
    }

    @Test
    void getItemsByList_Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lists/{id}/items", todoListId))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getItemsByList_NotFound() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
        setUserDetailForAuthentication(userDetails);

        when(todoListService.getItems(todoListId, userId)).thenThrow(new NotFoundException("Not found"));
        when(itemMapper.toItemGetDto(item)).thenReturn(itemGetDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/lists/{id}/items", todoListId))
            .andExpect(status().isNotFound());
    }

    private void setUserDetailForAuthentication(UserDetails userDetails) {
        when(userService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );
    }

}
