package app.syr.lib.book.service;

import app.syr.lib.Loan.entity.Loan;
import app.syr.lib.base.rsData.RsData;
import app.syr.lib.book.entity.Book;
import app.syr.lib.book.repository.BookRepository;
import app.syr.lib.category.entity.Category;
import app.syr.lib.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Service
@RequestMapping("/book")
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author).orElse(null);
    }

    public RsData<Book> create(String title, String author, String categoryName) {
        Category category = categoryService.findByName(categoryName);

        if (findByTitleAndAuthor(title, author) != null) {
            return RsData.of("F-1", "이미 존재하는 도서입니다.");
        }

        Book book = Book
                .builder()
                .title(title)
                .author(author)
                .category(category)
                .build();

        bookRepository.save(book);
        return RsData.of("S-1", "새로운 도서가 등록되었습니다.", book);
    }

    public RsData<Book> modify(Book book, String title, String author, String categoryName) {
        Category category = categoryService.findByName(categoryName);

        if (book.getTitle().equals(title) && book.getAuthor().equals(author)) {
            return RsData.of("F-1", "변경 사항이 없습니다.");
        }

        Book book1 = book
                .toBuilder()
                .title(title)
                .author(author)
                .category(category)
                .build();

        bookRepository.save(book1);
        return RsData.of("S-1", "도서 정보가 수정되었습니다.", book1);
    }

    // hard-delete
    public RsData delete(Book book) {
        String title = book.getTitle();
        bookRepository.delete(book);
        return RsData.of("S-1", "%s 도서가 삭제되었습니다.".formatted(title));
    }

    public void whenAfterLoan(Loan loan) {
        bookRepository.save(updateLoanStatus(loan.getBook(), true));
    }

    public void whenAfterReturn(Loan loan) {
        bookRepository.save(updateLoanStatus(loan.getBook(), false));
    }

    private Book updateLoanStatus(Book book, boolean bool) {
        return book
                .toBuilder()
                .isOnLoan(bool)
                .build();
    }
}
