package wanted.n.budgetmanager.server.repository.q;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;
import wanted.n.budgetmanager.server.domain.StatsSpdMonth;
import wanted.n.budgetmanager.server.dto.StatsSpdMonthDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static wanted.n.budgetmanager.server.domain.QCategory.category;
import static wanted.n.budgetmanager.server.domain.QStatsSpdMonth.statsSpdMonth;

@Repository
@RequiredArgsConstructor
public class StatsSpdMonthQRepositoryImpl implements StatsSpdMonthQRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public StatsSpdMonth getSumByCatIdList(StatsSpdMonthDTO statsSpdMonthDTO) {
        // 유저 id 조건
        JPQLQuery<StatsSpdMonth> query = queryFactory.select(Projections.fields(StatsSpdMonth.class
                        , statsSpdMonth.id, statsSpdMonth.userId, statsSpdMonth.date, statsSpdMonth.sum.sum().as("sum")))
                .from(statsSpdMonth)
                .where(statsSpdMonth.userId.eq(statsSpdMonthDTO.getUserId()));

        // 날짜 조건
        query.where(statsSpdMonth.date.eq(statsSpdMonthDTO.getDate()));

        // 카테고리 조건
        query.where(catIdEq(statsSpdMonthDTO.getCategoryList()));

        query.groupBy(statsSpdMonth.userId);

        return query.fetchOne();
    }

    @Override
    public List<SpdCatAmountVO> findSumListByDateAndUserIdOrderByCatId(LocalDate date, Long userId) {

        JPQLQuery<SpdCatAmountVO> query = queryFactory.select(Projections.fields(SpdCatAmountVO.class,
                        category.id.as("catId"),  new CaseBuilder()
                                .when(statsSpdMonth.sum.isNull())
                                .then(0L)
                                .otherwise(statsSpdMonth.sum).as("amount")
                ))
                .from(category)
                .leftJoin(statsSpdMonth).on(statsSpdMonth.catId.eq(category.id)
                        .and(statsSpdMonth.date.eq(date).or(statsSpdMonth.date.isNull())
                                .and(statsSpdMonth.userId.eq(userId).or(statsSpdMonth.userId.isNull()))))
                .orderBy(category.id.asc());

        return query.fetch();
    }

    @Override
    public Optional<Long> findAllSumByDateAndUserId(LocalDate date, Long userId) {
        JPQLQuery<Long> query = queryFactory.select(
                        statsSpdMonth.sum.sum())
                .from(statsSpdMonth)
                .where(statsSpdMonth.date.eq(date), statsSpdMonth.userId.eq(userId))
                .groupBy(statsSpdMonth.userId);

        return Optional.ofNullable(query.fetchOne());

    }

    @Override
    public Optional<Long> findAllSumByDate(LocalDate date) {
        JPQLQuery<Long> query = queryFactory.select(
                        statsSpdMonth.sum.sum())
                .from(statsSpdMonth)
                .where(statsSpdMonth.date.eq(date));

        return Optional.ofNullable(query.fetchOne());
    }

    private BooleanExpression catIdEq(List<Long> categoryList){
        if(!categoryList.isEmpty()){
            BooleanExpression init = statsSpdMonth.catId.eq(categoryList.get(0));

            for(int i=1; i<categoryList.size(); i++){
                init = statsSpdMonth.catId.eq(categoryList.get(i)).or(init);
            }

            return init;
        }

        return null;
    }
}
