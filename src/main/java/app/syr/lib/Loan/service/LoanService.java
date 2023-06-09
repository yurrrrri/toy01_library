package app.syr.lib.Loan.service;

import app.syr.lib.Loan.entity.Loan;
import app.syr.lib.Loan.repository.LoanRepository;
import app.syr.lib.Member.entity.Member;
import app.syr.lib.base.event.EventAfterLoan;
import app.syr.lib.base.event.EventAfterReturn;
import app.syr.lib.base.event.EventBeforeLoan;
import app.syr.lib.base.rq.Rq;
import app.syr.lib.base.rsData.RsData;
import app.syr.lib.book.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final ApplicationEventPublisher publisher;
    private final Rq rq;

    public Loan findById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    public List<Loan> findByMemberAndDeleteDateIsNull(Member member) {
        return loanRepository.findByMemberAndDeleteDateIsNull(member);
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public RsData<Loan> borrow(Book book) {
        publisher.publishEvent(new EventBeforeLoan(rq.getMember()));

        if (rq.getMember().isCannotUse())
            return RsData.of("F-1", "연체로 인해 대출할 수 없는 기간입니다.");

        if (book.isOnLoan())
            return RsData.of("F-2", "이미 대출 중인 도서입니다.");

        Loan loan = Loan
                .builder()
                .member(rq.getMember())
                .book(book)
                .build();

        String title = book.getTitle();
        loanRepository.save(loan);

        publisher.publishEvent(new EventAfterLoan(loan));
        return RsData.of("S-1", "%s 도서가 대출되었습니다.".formatted(title), loan);
    }

    public RsData<Loan> extend(Loan loan) {
        if (loan.isOverdue()) return RsData.of("F-2", "반납 기한이 지난 도서입니다.");

        LocalDateTime now = LocalDateTime.now();
        Loan extendLoan = loan
                .toBuilder()
                .deadline(now.plusDays(7))
                .build();
        loanRepository.save(extendLoan);
        return RsData.of("S-1", "반납 기한이 연장되었습니다.", extendLoan);
    }

    // hard-delete
    public RsData returnBook(Loan loan) {
        if (!rq.getMember().getUsername().equals(loan.getMember().getUsername())) {
            return RsData.of("F-1", "반납할 권한이 없습니다.");
        }

        if (loan.getMember() == null) {
            return RsData.of("F-2", "대출되지 않은 도서입니다.");
        }

        String title = loan.getBook().getTitle();

        publisher.publishEvent(new EventAfterReturn(loan));

        loanRepository.delete(loan);
        return RsData.of("S-1", "%s 도서가 반납되었습니다.".formatted(title));
    }
}
