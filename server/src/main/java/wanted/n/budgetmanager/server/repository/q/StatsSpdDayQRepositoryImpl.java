package wanted.n.budgetmanager.server.repository.q;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.StatsSpdDay;
import wanted.n.budgetmanager.server.dto.StatsSpdDayDTO;

import java.util.List;

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
