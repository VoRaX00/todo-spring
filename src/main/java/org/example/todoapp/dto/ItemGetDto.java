package org.example.todoapp.dto;

import lombok.*;

@Data
public class ItemGetDto {
    private Long id;
    private String title;
    private String description;
    private Boolean done;
}
