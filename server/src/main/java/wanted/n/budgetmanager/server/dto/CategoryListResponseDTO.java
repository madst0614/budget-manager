package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.budgetmanager.server.domain.Category;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryListResponseDTO {
    private List<Category> categoryList;

    public static CategoryListResponseDTO from(List<Category> categoryList){
        return CategoryListResponseDTO.builder().categoryList(categoryList).build();
    }
}
