package app.syr.lib.base.Controller;

import app.syr.lib.Loan.entity.Loan;
import app.syr.lib.Loan.service.LoanService;
import app.syr.lib.Member.entity.Member;
import app.syr.lib.Member.service.MemberService;
import app.syr.lib.base.rq.Rq;
import app.syr.lib.base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/adm")
public class AdmController {

    private final MemberService memberService;
    private final LoanService loanService;
    private final Rq rq;

    @GetMapping("/members")
    public String showMembers(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "adm/members";
    }

    @GetMapping("/loans")
    public String showLoans(Model model) {
        List<Loan> loans = loanService.findAll();
        model.addAttribute("loans", loans);
        return "adm/loans";
    }

    // 회원 삭제
    @GetMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        Member member = memberService.findById(id);

        if (member == null) return rq.historyBack("존재하지 않는 회원입니다.");

        RsData rs = memberService.deleteHard(member);
        return rq.redirectWithMsg("/adm/main", rs.getMsg());
    }
}
