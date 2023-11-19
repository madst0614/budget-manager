package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import wanted.n.budgetmanager.server.dto.CategoryListResponseDTO;
import wanted.n.budgetmanager.server.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**  카테고리 가져오기

        모든 카테고리 리스트 가져오기
     */
    @Cacheable(value="categoryList", key="'category'")
    public CategoryListResponseDTO getCategoryList(){

        return CategoryListResponseDTO.from(categoryRepository.findAll());
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
