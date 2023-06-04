package app.syr.lib.book.controller;

import app.syr.lib.base.rq.Rq;
import app.syr.lib.base.rsData.RsData;
import app.syr.lib.book.entity.Book;
import app.syr.lib.book.service.BookService;
import app.syr.lib.category.entity.Category;
import app.syr.lib.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Tag(name = "도서", description = "도서 목록, 생성, 수정, 삭제")
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final Rq rq;

    @GetMapping("/list")
    @Operation(summary = "도서 목록")
    public String showList(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book/list";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/create")
    @Operation(summary = "도서 생성")
    public String create(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "/book/create";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookForm {
        @NotBlank
        private String title;
        @NotBlank
        private String author;
        private String category;
    }

    @PostMapping("/create")
    @Operation(summary = "도서 생성")
    public String create(@Valid BookForm form) {
        RsData rs = bookService.create(form.getTitle(), form.getAuthor(), form.getCategory());
        return rq.redirectWithMsg("/book/list", rs.getMsg());
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/modify/{id}")
    @Operation(summary = "도서 수정")
    public String modify(Model model, @PathVariable Long id) {
        Book book = bookService.findById(id);
        model.addAttribute(book);

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "/book/modify";
    }

    @PostMapping("/modify/{id}")
    @Operation(summary = "도서 수정")
    public String modify(@Valid BookForm form, @PathVariable Long id) {
        Book book = bookService.findById(id);
        RsData rs = bookService.modify(book, form.getTitle(), form.getAuthor(), form.getCategory());

        if (rs.isFail()) return rq.historyBack(rs.getMsg());

        return rq.redirectWithMsg("/book/list", rs.getMsg());
    }

    // hard-delete
    @RolesAllowed("ADMIN")
    @GetMapping("/delete/{id}")
    @Operation(summary = "도서 삭제")
    public String delete(@PathVariable Long id) {
        Book book = bookService.findById(id);
        RsData rs = bookService.delete(book);
        return rq.redirectWithMsg("/book/list", rs.getMsg());
    }

}
