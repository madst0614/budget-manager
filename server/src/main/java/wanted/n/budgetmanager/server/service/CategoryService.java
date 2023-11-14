package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import wanted.n.budgetmanager.server.domain.Category;
import wanted.n.budgetmanager.server.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Cacheable(value="categoryList", key="'category'")
    public List<Category> getCategoryList(){
        return categoryRepository.findAll();
    }


    @CacheEvict(value="categoryList", key="'category'")
    public void createCategory(){
        // 관리 용도에 맞게 구현
    }

    @CacheEvict(value="categoryList", key="'category'")
    public void deleteCategory(){
        // 관리 용도에 맞게 구현
    }
}
