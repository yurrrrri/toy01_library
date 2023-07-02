package app.syr.lib.base.event;

import app.syr.lib.Member.entity.Member;
import lombok.Getter;

@Getter
public class EventBeforeLoan {

    private final Member member;

    public EventBeforeLoan(Member member) {
        this.member = member;
    }
}
