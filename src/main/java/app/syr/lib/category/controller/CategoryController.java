package app.syr.lib.category.controller;

import app.syr.lib.base.rq.Rq;
import app.syr.lib.base.rsData.RsData;
import app.syr.lib.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/category")
@Tag(name = "카테고리", description = "카테고리 목록, 생성, 수정, 삭제")
public class CategoryController {

    private final CategoryService categoryService;
    private final Rq rq;

    @GetMapping("/list")
    @Operation(summary = "카테고리 목록")
    public String showList(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "/category/list";
    }

    @GetMapping("/create")
    @Operation(summary = "카테고리 생성")
    public String create() {
        return "/category/create";
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryForm {
        @NotBlank
        private String name;
    }

    @PostMapping("/create")
    @Operation(summary = "카테고리 생성")
    public String create(@Valid CategoryForm form) {
        RsData rs = categoryService.create(form.getName());
        return rq.redirectWithMsg("/category/list", rs.getMsg());
    }

    @GetMapping("/modify/{id}")
    @Operation(summary = "카테고리 수정")
    public String modify(Model model, @PathVariable Long id) {
        model.addAttribute(categoryService.findById(id));
        return "/category/modify";
    }

    @PostMapping("/modify/{id}")
    @Operation(summary = "카테고리 수정")
    public String modify(@Valid CategoryForm form, @PathVariable Long id) {
        RsData rs = categoryService.modify(categoryService.findById(id), form.getName());
        return rq.redirectWithMsg("/category/list", rs.getMsg());
    }

    // hard-delete
    @GetMapping("/delete/{id}")
    @Operation(summary = "카테고리 삭제")
    public String delete(@PathVariable Long id) {
        RsData rs = categoryService.delete(categoryService.findById(id));
        return rq.redirectWithMsg("/category/list", rs.getMsg());
    }
}
