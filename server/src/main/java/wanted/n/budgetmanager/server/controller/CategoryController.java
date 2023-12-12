package wanted.n.budgetmanager.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.n.budgetmanager.server.domain.Category;
import wanted.n.budgetmanager.server.service.CategoryService;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/v1/categorys")
@Api(tags = "Category API", description = "카테고리와 관련된 API")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    @ApiOperation(value = "카테고리 가져오기", notes = "모든 카테고리 리스트를 가져옵니다")
    public ResponseEntity<List<Category>> getCategoryList(@RequestHeader(AUTHORIZATION) String token){

        return ResponseEntity.status(OK).body(categoryService.getCategoryList());
    }
}
