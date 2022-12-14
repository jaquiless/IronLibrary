package com.example.demo.repositories;


import com.example.demo.models.Book;
import com.example.demo.models.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {

    List<Issue> findByIssueStudentUsn(String usn);

}
