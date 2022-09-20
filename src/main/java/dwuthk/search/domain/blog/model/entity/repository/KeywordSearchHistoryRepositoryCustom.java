package dwuthk.search.domain.blog.model.entity.repository;

import dwuthk.search.domain.blog.model.KeywordSearchCountDTO;
import dwuthk.search.domain.blog.service.BlogSearchService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KeywordSearchHistoryRepositoryCustom {

    List<KeywordSearchCountDTO> findMostSearchedKeyword(BlogSearchService.KeywordStatisticsCondition condition, Pageable pageable);
}
