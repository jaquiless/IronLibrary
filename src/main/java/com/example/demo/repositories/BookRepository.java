package com.example.demo.repositories;


import com.example.demo.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository <Book, String> {

    Optional<Book> findByTitle(String title);
    List<Book> findByCategory(String category);
    List<Book> findByAuthorName(String authorName);

    Optional<Book> findByIsbn(String isbn);
}
