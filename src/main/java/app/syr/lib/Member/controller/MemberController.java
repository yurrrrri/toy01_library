package app.syr.lib.Member.controller;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.service.MemberService;
import app.syr.lib.base.rq.Rq;
import app.syr.lib.base.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
@Tag(name = "회원", description = "로그인, 회원가입 등")
public class MemberController {

    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/signup")
    @Operation(summary = "회원가입")
    public String signup() {
        return "/member/signup";
    }

    @Data
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
    @Operation(summary = "회원가입")
    public String signup(@Valid MemberCreateForm form) {
        RsData<Member> rs = memberService.create(form.getUsername(), form.getPassword1(), form.getPassword2(), form.getEmail(), form.getPhoneNumber());

        if (rs.isFail()) return rq.historyBack(rs.getMsg());

        return rq.redirectWithMsg("login", rs.getMsg());
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    @Operation(summary = "로그인")
    public String login() {
        return "/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    @Operation(summary = "마이페이지")
    public String showMypage(Model model) {
        Member member = rq.getMember();
        model.addAttribute("member", member);
        return "/member/mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    @Operation(summary = "회원 정보 수정")
    public String modify(Model model) {
        Member member = rq.getMember();
        model.addAttribute("member", member);
        return "member/modify";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberModifyForm {

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password1;

        @NotBlank(message = "비밀번호를 한 번 더 입력해주세요.")
        private String password2;

        @Email
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        private String phoneNumber;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    @Operation(summary = "회원 정보 수정")
    public String modify(@Valid MemberModifyForm form) {
        Member member = rq.getMember();
        RsData<Member> rs = memberService.modify(member, form.getPassword1(), form.getEmail(), form.getPhoneNumber());
        return rq.redirectWithMsg("mypage", rs.getMsg());
    }

    // soft-delete
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    @Operation(summary = "회원 탈퇴 (soft-delete 구현)")
    public String delete() {
        Member member = rq.getMember();
        RsData rs = memberService.delete(member);
        return "redirect:/logout";
    }

}
