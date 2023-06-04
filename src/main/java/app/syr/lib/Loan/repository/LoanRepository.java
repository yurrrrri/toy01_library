package app.syr.lib.Loan.repository;

import app.syr.lib.Loan.entity.Loan;
import app.syr.lib.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByMemberAndDeleteDateIsNull(Member member);
}
