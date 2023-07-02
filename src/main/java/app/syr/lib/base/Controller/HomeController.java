package app.syr.lib.base.Controller;

import app.syr.lib.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Tag(name = "메인", description = "메인, 로그아웃")
public class HomeController {

    private final BookService bookService;

    @GetMapping("/")
    @Operation(summary = "메인")
    public String main(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    @Operation(summary = "로그아웃")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/member/login";
    }
}
