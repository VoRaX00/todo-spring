package org.example.todoapp.dto;

import lombok.*;

@Data
@Builder
public class TodoListGetDto {
    private Long id;
    private String title;
    private String description;
}
