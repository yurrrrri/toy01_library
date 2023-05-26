package app.syr.lib.book.entity;

import app.syr.lib.base.entity.BaseEntity;
import app.syr.lib.category.entity.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Book extends BaseEntity {

    private String title;

    private String author;

    private boolean isBorrowed;

    @ManyToOne
    private Category category;
}