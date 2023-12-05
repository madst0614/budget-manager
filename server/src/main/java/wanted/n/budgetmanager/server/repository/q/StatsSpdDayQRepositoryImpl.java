package wanted.n.budgetmanager.server.repository.q;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;
import wanted.n.budgetmanager.server.domain.StatsSpdDay;
import wanted.n.budgetmanager.server.dto.StatsSpdDayDTO;

import java.time.LocalDate;
import java.util.List;

import static wanted.n.budgetmanager.server.domain.QCategory.category;
import static wanted.n.budgetmanager.server.domain.QStatsSpdDay.statsSpdDay;

@Repository
@RequiredArgsConstructor
public class StatsSpdDayQRepositoryImpl implements StatsSpdDayQRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public StatsSpdDay getSumByCatIdList(StatsSpdDayDTO statsSpdDayDTO) {

        // 유저 id 조건
        JPQLQuery<StatsSpdDay> query = queryFactory.select(Projections.fields(StatsSpdDay.class
                , statsSpdDay.id, statsSpdDay.userId, statsSpdDay.date, statsSpdDay.sum.sum().as("sum")))
                .from(statsSpdDay)
                .where(statsSpdDay.userId.eq(statsSpdDayDTO.getUserId()));

        // 날짜 조건
        query.where(statsSpdDay.date.eq(statsSpdDayDTO.getDate()));

        // 카테고리 조건
        query.where(catIdEq(statsSpdDayDTO.getCategoryList()));

        query.groupBy(statsSpdDay.userId);

        return query.fetchOne();
    }

    @Override
    public List<SpdCatAmountVO> findSumListByDateAndUserIdOrderByCatId(LocalDate date, Long userId) {

        JPQLQuery<SpdCatAmountVO> query = queryFactory.select(Projections.fields(SpdCatAmountVO.class,
                        category.id.as("catId"), new CaseBuilder()
                                .when(statsSpdDay.sum.isNull())
                                .then(0L)
                                .otherwise(statsSpdDay.sum).as("amount")
                ))
                .from(category)
                .leftJoin(statsSpdDay).on(statsSpdDay.catId.eq(category.id)
                        .and(statsSpdDay.date.eq(date).or(statsSpdDay.date.isNull())
                                .and(statsSpdDay.userId.eq(userId).or(statsSpdDay.userId.isNull()))))
                .orderBy(category.id.asc());

        return query.fetch();
    }

    private BooleanExpression catIdEq(List<Long> categoryList){
        if(!categoryList.isEmpty()){
            BooleanExpression init = statsSpdDay.catId.eq(categoryList.get(0));

            for(int i=1; i<categoryList.size(); i++){
                init = statsSpdDay.catId.eq(categoryList.get(i)).or(init);
            }

            return init;
        }

        return null;
    }
}
