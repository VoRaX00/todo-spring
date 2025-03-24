package org.example.todoapp.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DialectOverride;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String description;
    private Boolean done;
}
