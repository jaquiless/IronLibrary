package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;


import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;

    Book book1;
    Book book2;

    Author author1;
    Author author2;

    @BeforeEach
    void setUp()
    {
        book1 = bookRepository.save(new Book("1234", "Harry Potter", "Fiction", 4));
        book2 = bookRepository.save(new Book("4567", "100 Años", "Drama", 3));
        author1 = authorRepository.save(new Author("JK Rowling", "jk@rowling.com"));
        author2 = authorRepository.save(new Author("G Marquez", "g@marquez.com"));
    }

    @Test
    void shouldFindByTitle() {
        assertEquals("1234", bookRepository.findByTitle("Harry Potter").get().getIsbn());
    }

    @Test
    void shouldFindByCategory() {
        assertEquals(book2.getTitle(),bookRepository.findByCategory("Drama").get(0).getTitle());
    }

    @Test
    void shouldFindByIsbn() {
        assertTrue(bookRepository.findByIsbn("4567").get().getTitle().equals("100 Años"));
    }

    @Test
    void shouldFindByAuthorName() {
        book1.setAuthor(author1);
        bookRepository.save(book1);
        assertTrue(bookRepository.findByAuthorName("JK Rowling").get(0).getTitle().equals("Harry Potter"));
        book2.setAuthor(author2);
        bookRepository.save(book2);
        assertTrue(bookRepository.findByAuthorName("G Marquez").get(0).getTitle().equals("100 Años"));
    }

}