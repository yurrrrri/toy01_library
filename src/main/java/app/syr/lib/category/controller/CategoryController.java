package app.syr.lib.category.controller;

import app.syr.lib.base.rq.Rq;
import app.syr.lib.category.entity.Category;
import app.syr.lib.category.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final Rq rq;

    @GetMapping("/list")
    public String showList() {
        return "/category/list";
    }

    @GetMapping("/create")
    public String create() {
        return "/category/create";
    }

    @Data
    @AllArgsConstructor
    public static class CategoryForm {
        @NotBlank
        private String name;
    }

    @PostMapping("/create")
    public String create(@Valid CategoryForm form) {
        Category category = categoryService.create(form.getName());
        return rq.redirectWithMsg("/category/list", "새로운 카테고리가 생성되었습니다.");
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id) {
        return "/category/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@Valid CategoryForm form, @PathVariable Long id) {
        Category category = categoryService.findById(id);
        categoryService.modify(category, form.getName());
        return rq.redirectWithMsg("/category/list", "카테고리 이름이 수정되었습니다.");
    }

    // hard-delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        String name = category.getName();
        categoryService.delete(category);

        return rq.redirectWithMsg("/category/list", "%s 카테고리가 삭제되었습니다.".formatted(name));
    }
}
