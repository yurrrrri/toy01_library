package app.syr.lib.Loan.controller;

import app.syr.lib.Loan.entity.Loan;
import app.syr.lib.Loan.service.LoanService;
import app.syr.lib.Member.entity.Member;
import app.syr.lib.base.rq.Rq;
import app.syr.lib.base.rsData.RsData;
import app.syr.lib.book.entity.Book;
import app.syr.lib.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final Rq rq;

    @GetMapping("/list")
    public String showList(Model model) {
        Member member = rq.getMember();
        List<Loan> loans = member.getLoanList();

        model.addAttribute("loans", loans);
        return "/loan/list";
    }

    @PostMapping("/borrow/{id}")
    public String borrow(@PathVariable Long bookId) {
        Book book = bookService.findById(bookId);

        if (book == null) return rq.historyBack("존재하지 않는 도서입니다.");

        RsData<Loan> rs = loanService.borrow(rq.getMember(), book);

        if (rs.isFail()) return rq.historyBack(rs.getMsg());

        return rq.redirectWithMsg("/loan/list", rs.getMsg());
    }

    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long bookId) {
        Loan loan = loanService.findByBook_Id(bookId);

        if (loan == null) return rq.historyBack("존재하지 않는 대출 기록입니다.");

        RsData rs = loanService.returnBook(loan);

        if (rs.isFail()) return rq.historyBack(rs.getMsg());

        return rq.redirectWithMsg("/loan/list", rs.getMsg());
    }

}
