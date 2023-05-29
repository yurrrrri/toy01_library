package app.syr.lib.Loan.repository;

import app.syr.lib.Loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByBook_Id(Long id);
}
