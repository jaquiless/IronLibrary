package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.models.Author;
import com.example.demo.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

    @Test
    void shouldFindAuthorByEmail() {
        Author author = authorRepository.save(new Author("Susana", "susana@ironhack.com"));
        assertEquals(author.getName(), authorRepository.findByEmail("susana@ironhack.com").get().getName());
    }

}