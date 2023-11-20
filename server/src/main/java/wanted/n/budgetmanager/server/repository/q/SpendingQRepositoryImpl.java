package wanted.n.budgetmanager.server.repository.q;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.Category;
import wanted.n.budgetmanager.server.dto.SpendingBriefVO;
import wanted.n.budgetmanager.server.dto.SpendingListDTO;

import java.util.List;

import static wanted.n.budgetmanager.server.domain.QSpending.spending;

@Repository
@RequiredArgsConstructor
public class SpendingQRepositoryImpl implements SpendingQRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<SpendingBriefVO> getSpendingList(SpendingListDTO spendingListDTO) {

        // 유저 id 조건에 포함
        JPQLQuery<SpendingBriefVO> query = queryFactory.select(Projections.fields(SpendingBriefVO.class,
                spending.id, spending.userId, spending.catId
                        , spending.date, spending.amount, spending.excluded
                ))
                .from(spending)
                .where(spending.userId.eq(spendingListDTO.getUserId()));

        // 카테고리 조건에 포함
        query.where(catIdEq(spendingListDTO.getCategoryList()));

        // 날짜 조건 포함
        query.where(spending.date.goe(spendingListDTO.getStart())
                , spending.date.loe(spendingListDTO.getEnd()));

        // 최소 ~ 최대 금액 포함
        query.where(spending.amount.goe(spendingListDTO.getMin())
                , spending.amount.loe(spendingListDTO.getMax()));

        // 삭제 X
        query.where(spending.deleted.eq(false));

        return query.fetch();

    }

    private BooleanExpression catIdEq(List<Long> categoryList){
        if(!categoryList.isEmpty()){
            BooleanExpression init = spending.catId.eq(categoryList.get(0));

            for(int i=1; i<categoryList.size(); i++){
                init = spending.catId.eq(categoryList.get(i)).or(init);
            }

            return init;
        }

        return null;
    }
}
