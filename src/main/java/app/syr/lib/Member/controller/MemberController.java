package app.syr.lib.Member.controller;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/signup")
    public String signup() {
        return "/member/signup";
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class MemberCreateForm {

        @NotBlank(message = "ID를 입력해주세요.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password1;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password2;

        @Email
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        private String phoneNumber;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm) {
        memberService.create(memberCreateForm.getUsername(), memberCreateForm.getPassword1(), memberCreateForm.getEmail());
        return "redirect:/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login() {
        return "/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String showMypage() {
        return "/member/mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);

        if(member == null) {
            return "redirect:/member/login";
        }

        model.addAttribute("member", member);
        return "member/modify";
    }

}
