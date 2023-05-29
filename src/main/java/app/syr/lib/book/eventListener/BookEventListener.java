package app.syr.lib.book.eventListener;

import app.syr.lib.Member.service.MemberService;
import app.syr.lib.base.event.EventAfterLoan;
import app.syr.lib.base.event.EventAfterReturn;
import app.syr.lib.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class BookEventListener {

    private final BookService bookService;
    private final MemberService memberService;

    @EventListener
    public void listen(EventAfterLoan event) {
        bookService.whenAfterLoan(event.getLoan());
        memberService.whenAfterLoan(event.getLoan());
    }

    @EventListener
    public void listen(EventAfterReturn event) {
        bookService.whenAfterReturn(event.getLoan());
        memberService.whenAfterReturn(event.getLoan());
    }
}
