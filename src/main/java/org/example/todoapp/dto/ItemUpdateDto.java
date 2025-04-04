package org.example.todoapp.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateDto {
    private String title;
    private String description;
    private Boolean done;
}
