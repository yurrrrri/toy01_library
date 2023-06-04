package app.syr.lib.Member.eventListener;

import app.syr.lib.Member.service.MemberService;
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

}
