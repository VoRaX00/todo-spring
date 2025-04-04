package org.example.todoapp.dto;

import lombok.*;

@Data
@Builder
public class TodoListCreateDto {
    private String title;
    private String description;
}
