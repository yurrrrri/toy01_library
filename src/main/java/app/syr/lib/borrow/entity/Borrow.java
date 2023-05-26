package app.syr.lib.borrow.entity;

import app.syr.lib.Member.entity.Member;
import app.syr.lib.base.entity.BaseEntity;
import app.syr.lib.book.entity.Book;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Borrow extends BaseEntity {

    @ManyToOne
    private Member member;

    @OneToOne
    private Book book;

    private boolean overdue; // 연체되었는지?

}