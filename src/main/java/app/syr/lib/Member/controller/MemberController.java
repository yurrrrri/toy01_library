package app.syr.lib.Member.controller;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

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
    public static class MemberCreateForm {

        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(min = 4, max = 20)
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 6, max = 20)
        private String password1;

        @NotBlank(message = "비밀번호를 한 번 더 입력해주세요.")
        @Size(min = 6, max = 20)
        private String password2;

        @Email
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        private String phoneNumber;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/member/signup";

        if (!form.getPassword1().equals(form.getPassword2())) {
            bindingResult.reject("Password Incorrect", "2개의 비밀번호가 일치하지 않습니다.");
            return "/member/signup";
        }

        if (memberService.findByUsername(form.getUsername()) != null) {
            bindingResult.reject("Existing Username", "이미 존재하는 아이디입니다.");
            return "/member/signup";
        }

        memberService.create(form.getUsername(), form.getPassword1(), form.getEmail(), form.getPhoneNumber());
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberModifyForm {

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id) {
        Member member = memberService.findById(id);

        if (member == null) return "redirect:/member/login";

        return "member/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, MemberModifyForm form, Principal principal) {
        Member member = memberService.findById(id);

        if (!member.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다");
        }

        memberService.modify(member, form.getPassword1(), form.getEmail(), form.getPhoneNumber());
        return "member/mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal) {
        Member member = memberService.findById(id);

        if (!member.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다");
        }

        memberService.delete(member);
        return "/member/logout";
    }

}
