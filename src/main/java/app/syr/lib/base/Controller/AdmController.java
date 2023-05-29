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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/adm")
public class AdmController {

    private final MemberService memberService;
    private final LoanService loanService;
    private final Rq rq;

    @GetMapping("/main")
    public String showAdmMain() {
        return "/adm/main";
    }

    // 회원 삭제
    @GetMapping("/member/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        Member member = memberService.findById(id);

        if (member == null) return rq.historyBack("존재하지 않는 회원입니다.");

        RsData rs = memberService.deleteHard(member);
        return rq.redirectWithMsg("/adm/main", rs.getMsg());
    }

    // 대출 기록 삭제
    @GetMapping("/delete/{id}")
    public String deleteLoan(@PathVariable Long id) {
        Loan loan = loanService.findById(id);

        if (loan == null) return rq.historyBack("존재하지 않는 대출 기록입니다.");

        RsData rs = loanService.delete(loan);
        return rq.redirectWithMsg("/adm/main", rs.getMsg());
    }
}
