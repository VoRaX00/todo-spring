package org.example.todoapp.dto;

import lombok.*;

@Data
public class ItemUpdateDto {
    private String title;
    private String description;
    private Boolean done;
}
