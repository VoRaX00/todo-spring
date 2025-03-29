package org.example.todoapp.dto;

import lombok.*;

@Data
public class TodoListGetDto {
    private Long id;
    private String title;
    private String description;
}
