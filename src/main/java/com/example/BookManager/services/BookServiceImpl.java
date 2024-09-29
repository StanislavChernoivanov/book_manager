package com.example.BookManager.services;

import com.example.BookManager.model.entities.Book;
import com.example.BookManager.model.entities.Category;
import com.example.BookManager.model.repositories.BookRepository;
import com.example.BookManager.model.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@RequiredArgsConstructor
@Transactional
@CacheConfig(cacheManager = "redisCacheManager")
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;


    @Override
    @Cacheable(value = "entitiesByCategory", key = "#categoryName")
    public List<Book> findAllByCategoryName(String categoryName) {

        List<Book> books = bookRepository.findAllByCategoryName(categoryName);

        books.forEach(b -> b.   setCategoryName(b.getCategory().getName()));
        return books;
    }

    @Override
    @Cacheable(value = "entityByBookNameAndBookAuthor", key = "#name + #author")
    public Book findByNameAndAuthor(String name, String author) {
        Book book = Book.builder().author(author).name(name).build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact())
                .withMatcher("author" ,exact());
        Example<Book> example = Example.of(book, matcher);

       Book foundBook = bookRepository.findOne(example).orElseThrow(
                () -> new RuntimeException(MessageFormat.format(
                        "Книга автора {0} с названием {1} не найдена"
                        , author
                        , name
                )));
       foundBook.setCategoryName(foundBook.getCategory().getName());
       return foundBook;
    }

    @Override
    public Book create(Book book) {
        Category category = Category.builder().name(book.getCategoryName()).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact());
        Example<Category> example = Example.of(category, matcher);
        Optional<Category> foundCategory = categoryRepository.findOne(example);
        if (foundCategory.isEmpty()) {
            book.setCategory(categoryRepository.save(category));
            return bookRepository.save(book);
        }
        book.setCategory(foundCategory.get());
        return bookRepository.save(book);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "entitiesByCategory", key = "#book.categoryName"),
            @CacheEvict(value = "entityByBookNameAndBookAuthor"
                    , key = "#book.name + #book.author")
    })
    public Book update(Long id, Book book) throws IllegalAccessException {
        Book updatedBook = bookRepository.findById(id).orElseThrow(
                () -> new RuntimeException(MessageFormat.format(
                        "Книги с id {0} не найдено", id)
                )
            );
        Class<Book> bookClass = Book.class;
        Field[] fields = bookClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(book);
            if (value != null) field.set(updatedBook, value);
        }

        return bookRepository.save(updatedBook);

    }

    @Override
    public void delete(Long id) {
        Book evictBook = bookRepository.findById(id).orElseThrow(
                () -> new RuntimeException(MessageFormat.format(
                        "Книги с id {0} не найдено", id)
                )
        );

        clearEntitiesByCategoryCache(evictBook.getCategory().getName());
        clearEntityByBookNameAndByBookAuthorCache(
                evictBook.getName()
                , evictBook.getAuthor()
        );
        bookRepository.deleteById(id);
    }

    @CacheEvict(value = "entitiesByCategory", key = "#CategoryName")
    private void clearEntitiesByCategoryCache(String categoryName) {}

    @CacheEvict(value = "entityByBookNameAndBookAuthor", key = "#name + #author")
    private void clearEntityByBookNameAndByBookAuthorCache(
            String name, String author) {}
}
