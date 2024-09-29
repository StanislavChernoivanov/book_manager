package com.example.BookManager.model.repositories;

import com.example.BookManager.model.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b from Book b where b.category.name = :categoryName")
    List<Book> findAllByCategoryName(String categoryName);
}
