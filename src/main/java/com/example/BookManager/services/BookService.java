package com.example.BookManager.services;

import com.example.BookManager.model.entities.Book;

import java.util.List;

public interface BookService {

    List<Book> findAllByCategoryName(String categoryName);

    Book findByNameAndAuthor(String name, String author);

    Book create(Book book);

    Book update(Long id, Book book) throws IllegalAccessException;

    void delete(Long id);
}
