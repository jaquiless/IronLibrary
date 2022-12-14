package com.example.demo;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;


import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    void shouldFindByUsn() {
        Student student = studentRepository.save(new Student("S457", "Susana M"));
        assertEquals("Susana M", studentRepository.findByUsn("S457").get().getName());
    }

}