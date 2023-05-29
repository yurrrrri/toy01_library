package app.syr.lib.base.event;

import app.syr.lib.Loan.entity.Loan;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterLoan extends ApplicationEvent {

    private final Loan loan;

    public EventAfterLoan(Object source, Loan loan) {
        super(source);
        this.loan = loan;
    }
}
