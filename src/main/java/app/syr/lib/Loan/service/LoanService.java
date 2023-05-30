package app.syr.lib.Loan.service;

import app.syr.lib.Loan.entity.Loan;
import app.syr.lib.Loan.repository.LoanRepository;
import app.syr.lib.Member.entity.Member;
import app.syr.lib.base.event.EventAfterLoan;
import app.syr.lib.base.event.EventAfterReturn;
import app.syr.lib.base.rq.Rq;
import app.syr.lib.base.rsData.RsData;
import app.syr.lib.book.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final ApplicationEventPublisher publisher;
    private final Rq rq;

    public Loan findById(Long id) {
        Optional<Loan> loan = loanRepository.findById(id);

        if (loan.isEmpty()) return null;

        return loan.get();
    }

    public Loan findByBook_Id(Long bookId) {
        Optional<Loan> loan = loanRepository.findByBook_Id(bookId);

        if (loan.isEmpty()) return null;

        return loan.get();
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public RsData<Loan> borrow(Member member, Book book) {
        if (book.isOnLoan()) return RsData.of("F-1", "이미 대출 중인 도서입니다.");

        Loan loan = Loan
                .builder()
                .member(member)
                .book(book)
                .build();

        String title = book.getTitle();
        loanRepository.save(loan);

        publisher.publishEvent(new EventAfterLoan(this, loan));
        return RsData.of("S-1", "%s 도서가 대출되었습니다.".formatted(title), loan);
    }

    public RsData<Loan> extend(Loan loan) {
        if (loan.isOverdue()) return RsData.of("F-1", "반납 기한이 지난 도서입니다.");

        LocalDateTime now = LocalDateTime.now();
        Loan loan1 = loan
                .toBuilder()
                .modifyDate(now.plusDays(7))
                .build();
        loanRepository.save(loan1);
        return RsData.of("S-1", "반납 기한이 연장되었습니다.", loan1);
    }

    // soft-delete
    public RsData returnBook(Loan loan) {
        if (!rq.getMember().getUsername().equals(loan.getMember().getUsername())) {
            return RsData.of("F-1", "반납할 권한이 없습니다.");
        }

        if (loan.getMember() == null) {
            return RsData.of("F-2", "대출되지 않은 도서입니다.");
        }

        String title = loan.getBook().getTitle();

        Loan returned = loan
                .toBuilder()
                .deadline(null)
                .deleteDate(LocalDateTime.now())
                .build();
        loanRepository.save(returned);

        publisher.publishEvent(new EventAfterReturn(this, loan));
        return RsData.of("S-1", "%s 도서가 반납되었습니다.".formatted(title));
    }

    // hard-delete by Loan Entity
    public RsData delete(Loan loan) {
        Long id = loan.getId();
        loanRepository.delete(loan);

        return RsData.of("S-1", "%d번 대출 기록이 삭제되었습니다.".formatted(id));
    }
}
