package app.syr.lib.Loan.entity;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.base.entity.BaseEntity;
import app.syr.lib.book.entity.Book;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Loan extends BaseEntity {

    @ManyToOne
    private Member member;

    @OneToOne
    private Book book;

    private LocalDateTime deadline;

    public boolean isOverdue() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(deadline);
    }

}