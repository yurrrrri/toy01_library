package app.syr.lib.category.service;

import app.syr.lib.category.entity.Category;
import app.syr.lib.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) return null;

        return category.get();
    }

    public Category findByName(String name) {
        Optional<Category> category = categoryRepository.findByName(name);

        if (category.isEmpty()) return null;

        return category.get();
    }
}
