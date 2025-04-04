package org.example.todoapp.controllers;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.example.todoapp.dto.*;
import org.example.todoapp.mappers.*;
import org.example.todoapp.models.*;
import org.example.todoapp.services.*;
import org.example.todoapp.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;

//
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
    }

    @Test
    void getItem_Success() throws Exception {
        var userDetails = new UserDetailsImpl(
            userId, "testUser", "test@example.com", "1324",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(userService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        var token = jwtTokenUtils.generateToken(userDetails);

        when(itemService.getItemById(itemId, userId)).thenReturn(item);
        when(itemMapper.toItemGetDto(item)).thenReturn(itemGetDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                "/items/%s",
                itemId))
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(itemId))
            .andExpect(jsonPath("$.title").value("test-title"))
            .andExpect(jsonPath("$.description").value("test-description"))
            .andExpect(jsonPath("$.done").value(false))
            .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

}
