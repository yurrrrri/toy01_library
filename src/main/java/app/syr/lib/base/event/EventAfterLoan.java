package app.syr.lib.base.event;

import app.syr.lib.Loan.entity.Loan;
import lombok.Getter;

@Getter
public class EventAfterLoan {

    private final Loan loan;

    public EventAfterLoan(Loan loan) {
        this.loan = loan;
    }
}
