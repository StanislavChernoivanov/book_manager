package com.example.BookManager.controllers;

import com.example.BookManager.mappers.BookMapper;
import com.example.BookManager.model.entities.Book;
import com.example.BookManager.services.BookService;
import com.example.BookManager.web.model.BookListResponse;
import com.example.BookManager.web.model.BookResponse;
import com.example.BookManager.web.model.UpsertBookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    @GetMapping("/byCategoryName")
    public ResponseEntity<BookListResponse> findAllByCategoryName(
                                            @RequestParam String categoryName) {

        BookListResponse response = bookMapper.bookListToBookResponseList(
                bookService.findAllByCategoryName(categoryName));
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<BookResponse> findByNameAndAuthor(
                                            @RequestParam String bookName
                                            , @RequestParam String author
                                    ) {
        return ResponseEntity.ok(bookMapper.bookToResponse(
                bookService.findByNameAndAuthor(bookName, author)
        ));
    }


    @PostMapping
    public ResponseEntity<BookResponse> create(
                                    @RequestBody UpsertBookRequest request) {
        Book book = bookMapper.requestToBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                bookMapper.bookToResponse(bookService.create(book))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(
                                    @PathVariable Long id,
                                    @RequestBody UpsertBookRequest request
                            ) throws IllegalAccessException {
        Book book = bookMapper.requestToBook(id, request);
        return ResponseEntity.ok(bookMapper.bookToResponse(
                bookService.update(id, book)
        ));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
