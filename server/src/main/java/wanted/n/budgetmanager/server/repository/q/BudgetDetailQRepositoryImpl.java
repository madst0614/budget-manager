package wanted.n.budgetmanager.server.repository.q;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;

import java.util.List;

import static wanted.n.budgetmanager.server.domain.QBudget.budget;
import static wanted.n.budgetmanager.server.domain.QBudgetDetail.budgetDetail;

@Repository
@RequiredArgsConstructor
public class BudgetDetailQRepositoryImpl implements BudgetDetailQRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<SpdCatAmountVO> getSpdCatAmountVOListByUserIdOrderByCatId(Long userId) {
        JPQLQuery<SpdCatAmountVO> query = queryFactory.select(Projections.fields(SpdCatAmountVO.class,
                budgetDetail.catId, budgetDetail.amount
                ))
                .from(budgetDetail)
                .join(budget).on(budgetDetail.bgId.eq(budget.id))
                .where(budget.userId.eq(userId))
                .orderBy(budgetDetail.catId.asc());

        return query.fetch();
    }
}
