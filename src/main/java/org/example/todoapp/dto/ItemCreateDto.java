package org.example.todoapp.dto;

import lombok.*;

@Data
@Builder
public class ItemCreateDto {
    private String title;
    private String description;
    private Boolean done;
}
