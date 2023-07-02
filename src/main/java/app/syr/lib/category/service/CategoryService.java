package app.syr.lib.category.service;

import app.syr.lib.base.rsData.RsData;
import app.syr.lib.category.entity.Category;
import app.syr.lib.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
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
        Category modifyCategory = category
                .toBuilder()
                .name(name)
                .build();
        categoryRepository.save(modifyCategory);
        return RsData.of("S-1", "카테고리 이름이 수정되었습니다.", modifyCategory);
    }

    // hard-delete
    public RsData delete(Category category) {
        String name = category.getName();
        categoryRepository.delete(category);
        return RsData.of("S-1", "%s 카테고리가 삭제되었습니다.".formatted(name));
    }
}
