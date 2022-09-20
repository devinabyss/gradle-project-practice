package dwuthk.search.domain.blog.model.entity.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dwuthk.search.domain.blog.model.KeywordSearchCountDTO;
import dwuthk.search.domain.blog.service.BlogSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static dwuthk.search.domain.blog.model.entity.QKeywordSearchHistory.keywordSearchHistory;

@Slf4j
@Repository
@RequiredArgsConstructor
public class KeywordSearchHistoryRepositoryImpl implements KeywordSearchHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<KeywordSearchCountDTO> findMostSearchedKeyword(BlogSearchService.KeywordStatisticsConditionParams condition, Pageable pageable) {
        NumberPath<Long> aliasCount = Expressions.numberPath(Long.class, "cnt");

        return queryFactory
                .select(Projections.constructor(KeywordSearchCountDTO.class, keywordSearchHistory.keyword, keywordSearchHistory.count().as(aliasCount)))
                .from(keywordSearchHistory)
                .where(keywordSearchHistory.searchedAt.between(Objects.isNull(condition.getFrom()) ? LocalDateTime.now().minusDays(3) : condition.getFrom(), Objects.isNull(condition.getTo()) ? LocalDateTime.now() : condition.getTo()))
                .groupBy(keywordSearchHistory.keyword)
                .orderBy(aliasCount.desc())
                .orderBy(keywordSearchHistory.keyword.asc())
                .offset((long) (pageable.getPageNumber() - 1) * pageable.getPageSize())
                .limit((long) pageable.getPageNumber() * pageable.getPageSize())
                .fetch();

    }
}
