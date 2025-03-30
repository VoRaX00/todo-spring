package org.example.todoapp.dto;

import lombok.*;

@Data
public class ItemCreateDto {
    private String title;
    private String description;
    private Boolean done;
}
