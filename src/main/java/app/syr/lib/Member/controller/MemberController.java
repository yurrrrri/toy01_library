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
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
@Tag(name = "MemberController", description = "로그인, 회원가입 등")
public class MemberController {

    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/signup")
    @Operation(summary = "회원가입")
    public String signup() {
        return "/member/signup";
    }

    @Getter
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

        return rq.redirectWithMsg("/member/login", rs.getMsg());
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
    @GetMapping("/modify/{id}")
    @Operation(summary = "회원 정보 수정")
    public String modify(@PathVariable Long id) {
        Member member = memberService.findByIdAndDeleteDateIsNull(id);

        if (member == null) {
            return rq.historyBack("존재하지 않는 회원입니다.");
        }

        return "member/modify";
    }

    @Getter
    @Setter
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
    @PostMapping("/modify/{id}")
    @Operation(summary = "회원 정보 수정")
    public String modify(@PathVariable Long id, @Valid MemberModifyForm form) {
        Member member = memberService.findByIdAndDeleteDateIsNull(id);

        if (!member.getUsername().equals(rq.getMember().getUsername())) {
            return rq.historyBack("수정 권한이 없습니다.");
        }

        RsData<Member> rs = memberService.modify(member, form.getPassword1(), form.getEmail(), form.getPhoneNumber());
        return rq.redirectWithMsg("member/mypage", rs.getMsg());
    }

    // soft-delete
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    @Operation(summary = "회원 삭제 - soft")
    public String delete(@PathVariable Long id) {
        Member member = memberService.findByIdAndDeleteDateIsNull(id);

        if (member == null) {
            return rq.historyBack("존재하지 않는 회원입니다.");
        }

        if (!member.getUsername().equals(rq.getMember().getUsername())) {
            return rq.historyBack("삭제 권한이 없습니다.");
        }

        RsData rs = memberService.delete(member);
        return rq.redirectWithMsg("member/logout", rs.getMsg());
    }

}
