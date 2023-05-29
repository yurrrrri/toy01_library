package app.syr.lib.category.entity;

import app.syr.lib.base.entity.BaseEntity;
import app.syr.lib.book.entity.Book;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Category extends BaseEntity {

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Book> books = new ArrayList<>();

}