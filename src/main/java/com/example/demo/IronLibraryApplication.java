package com.example.demo;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class IronLibraryApplication implements CommandLineRunner {
	@Autowired
	AuthorRepository authorRepository;
	@Autowired
	BookRepository bookRepository;
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	IssueRepository issueRepository;

	public static void main(String[] args) {
		SpringApplication.run(IronLibraryApplication.class, args);
	}

	public static void runIronLibrary(AuthorRepository authorRepository, BookRepository bookRepository, StudentRepository studentRepository, IssueRepository issueRepository) {
		Scanner sc = new Scanner(System.in);

		int options = 1;
		while (options != 0) {
			//command menu
			System.out.println(
					"Choose an option\n" +
							"1 Add a book\n" +
							"2 Search book by title\n" +
							"3 Search book by category\n" +
							"4 Search book by author\n" +
							"5 List all books along with author\n" +
							"6 Issue book to student\n" +
							"7 List books by usn\n" +
							"8 Exit");

			options = sc.nextInt();

			switch (options) {
				case 1: {
					//Add a book
					addBook(authorRepository, bookRepository);
					break;
				}
				case 2: {
					//Search book by title
					System.out.println(searchBookByTitle(bookRepository));
					break;
				}
				case 3: {
					//Search book by category
					System.out.println(searchBookByCategory(bookRepository));
					break;
				}
				case 4: {
					//Search book by author
					searchBookByAuthor(bookRepository);
					break;
				}
				case 5: {
					//List all books along with author
					listBooksWithAuthor(bookRepository);
					break;
				}
				case 6: {
					//Issue book to student
					issueBookStudent(studentRepository, bookRepository, issueRepository);
					break;
				}
				case 7: {
					//List books by usn
					listBooksUsn(studentRepository, bookRepository, issueRepository);
					break;
				}
				case 8: {
					//Exit
					System.out.println("Finalizing library system...");
					System.exit(1);
					break;
				}
				default:
					//Error
					System.out.println("Choose a correct option between 1 and 8");
					break;
			}
		}
	}

	public static void addBook(AuthorRepository authorRepository, BookRepository bookRepository) {

		Scanner sc = new Scanner(System.in);
		System.out.println("Please introduce a isbn: ");
		String isbnBook = sc.nextLine();
		System.out.println("Please introduce a title: ");
		String titleBook = sc.nextLine();
		System.out.println("Please introduce a category: ");
		String categoryBook = sc.nextLine();
		System.out.println("Please introduce the name of the author: ");
		String nameAuthor = sc.nextLine();
		System.out.println("Please introduce the mail of the author: ");
		String mailAuthor = sc.nextLine();
		System.out.println("Please introduce the number of books: ");
		Integer numberCopiesBook = sc.nextInt();

		//create and save new book
		Book book = bookRepository.save(new Book(isbnBook, titleBook, categoryBook, numberCopiesBook));

		//create and save new author
		if (authorRepository.findByEmail(mailAuthor).isPresent()) {
			Author author = authorRepository.findByEmail(mailAuthor).get();
			book.setAuthor(author);
			bookRepository.save(book);
		} else {
			Author author = authorRepository.save(new Author(nameAuthor, mailAuthor));
			//set author to book and save it to repository
			book.setAuthor(author);
			bookRepository.save(book);
		}
		sc.nextLine();
	}

	public static Book searchBookByTitle(BookRepository bookRepository) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce the title of the book you want: ");
		String titleBook = sc.next();
		if (bookRepository.findByTitle(titleBook).isPresent()) {
			return bookRepository.findByTitle(titleBook).get();
		}
		System.err.println("Title not found");
		return null;
	}

	public static List<Book> searchBookByCategory(BookRepository bookRepository) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce the category of the book you want: ");
		String categoryBook = sc.next();
		List<Book> booksByCategory = bookRepository.findByCategory(categoryBook);
		if (booksByCategory.isEmpty()) {
			System.err.println("No books in this category ");
		}
		return booksByCategory;
	}

	public static void searchBookByAuthor(BookRepository bookRepository) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce the name of the author you want: ");
		String authorName = sc.nextLine();
		if (bookRepository.findByAuthorName(authorName).size() > 0) {
			for (Book b : bookRepository.findByAuthorName(authorName)) {
				System.out.println(b);
			}
		} else {
			System.err.println("No books from this author ");
		}
	}

	public static void listBooksWithAuthor(BookRepository bookRepository) {
		System.out.println("Here are all the books available in our library: ");
		for (Book b : bookRepository.findAll()) {
			System.out.println(b);
		}
	}

	public static void issueBookStudent(StudentRepository studentRepository, BookRepository bookRepository, IssueRepository issueRepository) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce the USN of the student: ");
		String studentUsn = sc.next();
		System.out.println("Introduce the name of the student: ");
		String studentName = sc.next();
		System.out.println("Introduce the ISBN of the book: ");
		String bookIssueISBN = sc.next();
		if (bookRepository.findByIsbn(bookIssueISBN).isPresent() && studentRepository.findByUsn(studentUsn).isPresent()) {
			Student student = studentRepository.findByUsn(studentUsn).get();
			Book book = bookRepository.findByIsbn(bookIssueISBN).get();
			if (book.getQuantity() > 0) {
				book.setQuantity(book.getQuantity() - 1);
				bookRepository.save(book);
				Issue issue = new Issue(LocalDateTime.now().toString(), LocalDateTime.now().plusDays(7).toString(), student, book);
				issueRepository.save(issue);
				student.getIssueList().add(issue);
				studentRepository.save(student);
				System.out.println("Book issued. Return date : " + issue.getReturnDate());
			} else {
				System.err.println("No book issues available for this ISBN");
			}
		} else if (bookRepository.findByIsbn(bookIssueISBN).isPresent() && studentRepository.findByUsn(studentUsn).isEmpty()) {
			Student student = studentRepository.save(new Student(studentUsn, studentName));
			Book book = bookRepository.findByIsbn(bookIssueISBN).get();
			book.setQuantity(book.getQuantity() - 1);
			bookRepository.save(book);
			Issue issue = new Issue(LocalDateTime.now().toString(), LocalDateTime.now().plusDays(7).toString(), student, book);
			issueRepository.save(issue);
			student.getIssueList().add(issue);
			studentRepository.save(student);
			System.out.println("Book issued. Return date : " + issue.getReturnDate());
		} else {
			System.err.println("ISBN not available");
		}
	}

	public static void listBooksUsn(StudentRepository studentRepository, BookRepository bookRepository, IssueRepository issueRepository) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce the USN of the student: ");
		String studentUsn = sc.nextLine();
		if (studentRepository.findByUsn(studentUsn).isPresent()) {
			for (Issue i : issueRepository.findByIssueStudentUsn(studentUsn)) {
				System.out.println(i);
			}
		} else {
			System.err.println("USN not found");
		}
	}

	@Override
	public void run(String... args) throws Exception {
		runIronLibrary(authorRepository, bookRepository, studentRepository, issueRepository);
	}
}
