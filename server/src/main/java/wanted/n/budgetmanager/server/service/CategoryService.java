package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.budgetmanager.server.domain.Category;
import wanted.n.budgetmanager.server.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**  카테고리 가져오기

        모든 카테고리 리스트 가져오기
     */
    @Cacheable(value="categoryList", key="'category'")
    @Transactional
    public List<Category> getCategoryList(){

        return categoryRepository.findAllByOrderByIdAsc();
    }

    @Cacheable(value="category", key="'categoryId'")
    @Transactional
    public Optional<Category> getCategory(Long id){

        return categoryRepository.findById(id);
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
