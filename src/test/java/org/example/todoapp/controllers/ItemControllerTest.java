package org.example.todoapp.controllers;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.example.todoapp.dto.*;
import org.example.todoapp.exceptions.*;
import org.example.todoapp.mappers.*;
import org.example.todoapp.models.*;
import org.example.todoapp.services.*;
import org.example.todoapp.utils.*;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemMapper itemMapper;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private final Long userId = 1L;

    private final Long itemId = 10L;

    private Item item;

    private ItemGetDto itemGetDto;

    private ItemUpdateDto itemUpdateDto;

    private Item itemUpdated;

    @BeforeEach
    void setUp() {
        item = Item.builder()
            .id(itemId).title("test-title")
            .description("test-description").done(false).build();

        itemGetDto = ItemGetDto.builder()
            .id(itemId).title("test-title")
            .description("test-description").done(false).build();

        itemUpdateDto = ItemUpdateDto.builder()
            .title("test-upd-title").description("test-upd-description")
            .done(true).build();

        itemUpdated = item;
        itemUpdated.setTitle(itemUpdateDto.getTitle());
        itemUpdated.setDescription(itemUpdateDto.getDescription());
        itemUpdated.setDone(itemUpdateDto.getDone());
    }

    @Test
    void getItem_Success() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        setUserDetailForAuthentication(userDetails);

        when(itemService.getItemById(itemId, userId)).thenReturn(item);
        when(itemMapper.toItemGetDto(item)).thenReturn(itemGetDto);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                    "/items/%s",
                    itemId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(itemId))
            .andExpect(jsonPath("$.title").value("test-title"))
            .andExpect(jsonPath("$.description").value("test-description"))
            .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void getItem_NotFound() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        setUserDetailForAuthentication(userDetails);

        when(itemService.getItemById(itemId, userId)).thenThrow(new NotFoundException("Not found"));
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                    "/items/%s",
                    itemId)))
            .andExpect(status().isNotFound());
    }

    @Test
    void getItem_Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                "/items/%s",
                itemId)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void updateItem_Success() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        setUserDetailForAuthentication(userDetails);

        when(itemMapper.toModel(itemUpdateDto)).thenReturn(itemUpdated);
        doNothing().when(itemService).saveItem(itemUpdated, userId);
        mockMvc.perform(MockMvcRequestBuilders
                .put(String.format("/items/%s", itemUpdated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemUpdateDto)))
            .andExpect(status().isOk());
    }

    @Test
    void updateItem_NotFound() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        setUserDetailForAuthentication(userDetails);
        when(itemMapper.toModel(itemUpdateDto)).thenReturn(itemUpdated);
        doThrow(new NotFoundException("Not found")).when(itemService).saveItem(itemUpdated, userId);
        mockMvc.perform(MockMvcRequestBuilders
                .put(String.format("/items/%s", itemUpdated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemUpdateDto)))
            .andExpect(status().isNotFound());

    }

    @Test
    void updateItem_Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put(String.format("/items/%s", itemUpdated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemUpdateDto)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteItem_Success() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        setUserDetailForAuthentication(userDetails);

        doNothing().when(itemService).deleteItem(itemId, userId);
        mockMvc.perform(MockMvcRequestBuilders
                .delete(String.format("/items/%s", itemId)))
            .andExpect(status().isOk());
    }

    @Test
    void deleteItem_NotFound() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        setUserDetailForAuthentication(userDetails);
        doThrow(new NotFoundException("Not found")).when(itemService).deleteItem(itemId, userId);
        mockMvc.perform(MockMvcRequestBuilders
                .delete(String.format("/items/%s", itemId)))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteItem_Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete(String.format("/items/%s", itemId)))
            .andExpect(status().isUnauthorized());
    }

    private void setUserDetailForAuthentication(UserDetails userDetails) {
        when(userService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );
    }

}
