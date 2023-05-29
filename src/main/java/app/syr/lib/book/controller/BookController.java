package app.syr.lib.book.controller;

import app.syr.lib.base.rq.Rq;
import app.syr.lib.book.entity.Book;
import app.syr.lib.book.service.BookService;
import app.syr.lib.category.entity.Category;
import app.syr.lib.category.service.CategoryService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final Rq rq;

    @GetMapping("/list")
    public String showList() {
        return "book/list";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/create")
    public String create(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "/book/create";
    }

    @Data
    @AllArgsConstructor
    public static class BookForm {
        @NotBlank
        private String title;
        @NotBlank
        private String author;
        @NotBlank
        private String category;
    }

    @PostMapping("/create")
    public String create(@Valid BookForm form) {
        Book book = bookService.create(form.getTitle(), form.getAuthor(), form.getCategory());
        return rq.redirectWithMsg("/book/list", "새로운 도서가 등록되었습니다.");
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id) {
        return "/book/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@Valid BookForm form, @PathVariable Long id) {
        Book book = bookService.findById(id);
        bookService.modify(book, form.getTitle(), form.getAuthor(), form.getCategory());
        return rq.redirectWithMsg("/book/list", "도서 정보가 수정되었습니다.");
    }

    // hard-delete
    @RolesAllowed("ADMIN")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Book book = bookService.findById(id);
        String title = book.getTitle();
        bookService.delete(book);

        return rq.redirectWithMsg("/book/list", "%s 도서가 삭제되었습니다.".formatted(title));
    }

}
