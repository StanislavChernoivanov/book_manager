package com.example.BookManager.mappers;

import com.example.BookManager.model.entities.Book;
import com.example.BookManager.web.model.BookListResponse;
import com.example.BookManager.web.model.BookResponse;
import com.example.BookManager.web.model.UpsertBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {


    Book requestToBook(UpsertBookRequest upsertBookRequest);
    @Mapping(source = "bookId", target = "id")
    Book requestToBook(Long bookId, UpsertBookRequest upsertBookRequest);

    BookResponse bookToResponse(Book book);

    List<BookResponse> bookListToResponseList(List<Book> books);

    default BookListResponse bookListToBookResponseList(List<Book> books) {
        BookListResponse bookListResponse = new BookListResponse();
        bookListResponse.setBookResponseList(bookListToResponseList(books));
        return bookListResponse;
    }
}
