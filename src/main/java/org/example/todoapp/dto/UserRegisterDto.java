package org.example.todoapp.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    private String email;
    private String username;
    private String password;
}
