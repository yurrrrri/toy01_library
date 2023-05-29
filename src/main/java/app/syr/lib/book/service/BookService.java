package app.syr.lib.book.service;

import app.syr.lib.Loan.entity.Loan;
import app.syr.lib.book.entity.Book;
import app.syr.lib.book.repository.BookRepository;
import app.syr.lib.category.entity.Category;
import app.syr.lib.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@RequestMapping("/book")
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    public Book findById(Long id) {
        Optional<Book> book = bookRepository.findById(id);

        if (book.isEmpty()) return null;

        return book.get();
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book create(String title, String author, String category) {
        Category category1 = categoryService.findByName(category);

        Book book = Book
                .builder()
                .title(title)
                .author(author)
                .category(category1)
                .build();

        bookRepository.save(book);
        return book;
    }

    public Book modify(Book book, String title, String author, String category) {
        Category category1 = categoryService.findByName(category);

        Book book1 = book
                .toBuilder()
                .title(title)
                .author(author)
                .category(category1)
                .build();

        bookRepository.save(book1);
        return book1;
    }

    // hard-delete
    public String delete(Book book) {
        String title = book.getTitle();
        bookRepository.delete(book);
        return title;
    }

    public void whenAfterLoan(Loan loan) {
        Book book = loan.getBook();
        Book book1 = book
                .toBuilder()
                .isOnLoan(true)
                .build();
        bookRepository.save(book1);
    }

    public void whenAfterReturn(Loan loan) {
        Book book = loan.getBook();
        Book book1 = book
                .toBuilder()
                .isOnLoan(false)
                .build();
        bookRepository.save(book1);
    }
}
