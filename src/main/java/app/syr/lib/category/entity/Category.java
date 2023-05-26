package app.syr.lib.category.entity;

import app.syr.lib.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Category extends BaseEntity {

    private String name;
}