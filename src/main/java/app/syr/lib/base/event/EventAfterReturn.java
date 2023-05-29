package app.syr.lib.base.event;

import app.syr.lib.Loan.entity.Loan;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterReturn extends ApplicationEvent {

    private final Loan loan;

    public EventAfterReturn(Object source, Loan loan) {
        super(source);
        this.loan = loan;
    }
}
