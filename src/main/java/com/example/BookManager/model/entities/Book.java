package com.example.BookManager.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name ="books", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "author" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String author;

    @JoinColumn(name = "category_id")
    @ManyToOne()
    private Category category;
    @Transient
    private String categoryName;
}
