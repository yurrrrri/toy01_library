package app.syr.lib.category.service;

import app.syr.lib.base.rsData.RsData;
import app.syr.lib.category.entity.Category;
import app.syr.lib.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public RsData<Category> create(String name) {
        Category category = Category
                .builder()
                .name(name)
                .build();

        categoryRepository.save(category);
        return RsData.of("S-1", "카테고리가 생성되었습니다.", category);
    }

    public RsData<Category> modify(Category category, String name) {
        Category category1 = category
                .toBuilder()
                .name(name)
                .build();
        categoryRepository.save(category1);
        return RsData.of("S-1", "카테고리 이름이 수정되었습니다.", category1);
    }

    // hard-delete
    public RsData delete(Category category) {
        String name = category.getName();
        categoryRepository.delete(category);
        return RsData.of("S-1", "%s 카테고리가 삭제되었습니다.".formatted(name));
    }
}
