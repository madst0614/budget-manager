package wanted.n.budgetmanager.server.repository.q;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<SpdCatAmountVO> getAllSumGroupByCatIdOrderBySum() {
        StringPath aliasSum = Expressions.stringPath("amount");

        JPQLQuery<SpdCatAmountVO> query = queryFactory.select(Projections.fields(SpdCatAmountVO.class,
                        budgetDetail.catId, budgetDetail.amount.sum().as("amount")
                ))
                .from(budgetDetail)
                .groupBy(budgetDetail.catId)
                .orderBy(aliasSum.desc());

        return query.fetch();
    }

    @Override
    public Optional<Long> getAllSum() {
        JPQLQuery<Long> query = queryFactory.select(
                        budgetDetail.amount.sum()
                )
                .from(budgetDetail);

        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public Optional<Long> getAllSumByUserId(Long userId) {
        JPQLQuery<Long> query =
                queryFactory
                        .select(budgetDetail.amount.sum())
                .from(budgetDetail)
                .join(budget).on(budgetDetail.bgId.eq(budget.id))
                .where(budget.userId.eq(userId));

        return Optional.ofNullable(query.fetchOne());
    }
}
