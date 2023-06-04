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
import org.springframework.web.bind.annotation.*;

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
        List<Loan> loans = loanService.findByMemberAndDeleteDateIsNull(member);

        model.addAttribute("loans", loans);
        return "loan/list";
    }

    @PostMapping("/borrow")
    public String borrow(@RequestParam Long bookId) {
        Book book = bookService.findById(bookId);

        if (book == null) return rq.historyBack("존재하지 않는 도서입니다.");

        RsData<Loan> rs = loanService.borrow(book);

        if (rs.isFail()) return rq.historyBack(rs.getMsg());

        return rq.redirectWithMsg("/loan/list", rs.getMsg());
    }

    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        Loan loan = loanService.findById(id);

        if (loan == null) return rq.historyBack("존재하지 않는 대출 기록입니다.");

        RsData rs = loanService.returnBook(loan);

        if (rs.isFail()) return rq.historyBack(rs.getMsg());

        return rq.redirectWithMsg("/loan/list", rs.getMsg());
    }

}
