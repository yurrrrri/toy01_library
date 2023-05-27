package app.syr.lib.book.controller;

import app.syr.lib.base.rq.Rq;
import app.syr.lib.book.entity.Book;
import app.syr.lib.book.service.BookService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final Rq rq;

    @GetMapping("/list")
    public String showList() {
        return "book/list";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/add")
    public String create() {
        return "/book/add";
    }

    @Data
    @AllArgsConstructor
    public static class BookCreateForm {
        @NotBlank
        private String title;
        @NotBlank
        private String author;
        @NotBlank
        private String category;
    }

    @PostMapping("/add")
    public String create(@Valid BookCreateForm form) {
        Book book = bookService.create(form.getTitle(), form.getAuthor(), form.getCategory());
        return rq.redirectWithMsg("/book/list", "새로운 도서가 등록되었습니다.");
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id) {
        return "/book/modify";
    }

    @Data
    @AllArgsConstructor
    public static class BookModifyForm {
        @NotBlank
        private String title;
        @NotBlank
        private String author;
        @NotBlank
        private String category;
    }

    @PostMapping("/modify/{id}")
    public String modify(@Valid BookModifyForm form, @PathVariable Long id) {
        Book book = bookService.findById(id);
        bookService.modify(book, form.getTitle(), form.getAuthor(), form.getCategory());
        return rq.redirectWithMsg("/book/list", "도서 정보가 수정되었습니다.");
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Book book = bookService.findById(id);
        bookService.delete(book);

        return rq.redirectWithMsg("/book/list", "도서가 삭제되었습니다.");
    }

}
