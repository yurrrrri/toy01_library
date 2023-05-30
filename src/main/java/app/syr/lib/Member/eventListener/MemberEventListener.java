package app.syr.lib.Member.eventListener;

import app.syr.lib.Member.service.MemberService;
import app.syr.lib.base.event.EventAfterLoan;
import app.syr.lib.base.event.EventAfterReturn;
import app.syr.lib.base.event.EventBeforeLoan;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class MemberEventListener {

    private final MemberService memberService;

    @EventListener
    public void listen(EventBeforeLoan event) {
        memberService.whenBeforeLoan(event.getMember());
    }

    @EventListener
    public void listen(EventAfterLoan event) {
        memberService.whenAfterLoan(event.getLoan());
    }

    @EventListener
    public void listen(EventAfterReturn event) {
        memberService.whenAfterReturn(event.getLoan());
    }

}
