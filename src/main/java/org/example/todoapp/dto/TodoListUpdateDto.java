package org.example.todoapp.dto;

import lombok.*;

@Data
@Builder
public class TodoListUpdateDto {
    private String title;
    private String description;
}
