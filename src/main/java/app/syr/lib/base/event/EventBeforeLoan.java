package app.syr.lib.base.event;

import app.syr.lib.Member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventBeforeLoan extends ApplicationEvent {

    private final Member member;

    public EventBeforeLoan(Object source, Member member) {
        super(source);
        this.member = member;
    }
}
