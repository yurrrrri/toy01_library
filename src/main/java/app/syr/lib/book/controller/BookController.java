package app.syr.lib.book.controller;

import app.syr.lib.book.entity.Book;
import app.syr.lib.book.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @GetMapping("/list")
    public String showList() {
        return "book/list";
    }

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
        return "redirect:/book/list";
    }

}
