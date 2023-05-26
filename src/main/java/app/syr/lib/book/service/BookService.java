package app.syr.lib.book.service;

import app.syr.lib.book.entity.Book;
import app.syr.lib.book.repository.BookRepository;
import app.syr.lib.category.entity.Category;
import app.syr.lib.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Service
@RequestMapping("/book")
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;

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

}
