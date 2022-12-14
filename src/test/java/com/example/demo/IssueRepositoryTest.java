package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;


import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class IssueRepositoryTest {

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    BookRepository bookRepository;

    Student student;
    Book book;

    Issue issue;

    @BeforeEach
    void setUp() {
        student = studentRepository.save(new Student("S457", "Susana M"));
        book = bookRepository.save(new Book("1234", "Harry Potter", "Fiction", 4));
        issue = issueRepository.save(new Issue(LocalDateTime.now().toString(), LocalDateTime.now().plusDays(7).toString(), student, book));
    }

    @Test
    void shouldFindByIssueStudentUsn() {
        issue.setIssueStudent(student);
        issueRepository.save(issue);
        assertEquals("Harry Potter", issueRepository.findByIssueStudentUsn("S457").get(0).getIssueBook().getTitle());
    }

}