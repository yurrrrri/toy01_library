package app.syr.lib.base.event;

import app.syr.lib.Loan.entity.Loan;
import lombok.Getter;

@Getter
public class EventAfterReturn {

    private final Loan loan;

    public EventAfterReturn(Loan loan) {
        this.loan = loan;
    }
}
