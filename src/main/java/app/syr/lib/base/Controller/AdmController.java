package app.syr.lib.base.Controller;

import app.syr.lib.Loan.service.LoanService;
import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.service.MemberService;
import app.syr.lib.base.rq.Rq;
import app.syr.lib.base.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/adm")
@Tag(name = "관리자 메인", description = "회원 목록, 대출 현황, 회원 정보 삭제")
public class AdmController {

    private final MemberService memberService;
    private final LoanService loanService;
    private final Rq rq;

    @GetMapping("/members")
    @Operation(summary = "전체 회원 목록")
    public String showMembers(Model model) {
        model.addAttribute("members", memberService.findAll());
        return "adm/members";
    }

    @GetMapping("/loans")
    @Operation(summary = "대출 현황")
    public String showLoans(Model model) {
        model.addAttribute("loans", loanService.findAll());
        return "adm/loans";
    }

    @GetMapping("/members/delete/{id}")
    @Operation(summary = "회원 정보 삭제")
    public String deleteMember(@PathVariable Long id) {
        Member member = memberService.findById(id);

        if (member == null) return rq.historyBack("존재하지 않는 회원입니다.");

        RsData rs = memberService.deleteHard(member);
        return rq.redirectWithMsg("/adm/main", rs.getMsg());
    }
}
