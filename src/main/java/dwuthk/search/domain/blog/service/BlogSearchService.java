package dwuthk.search.domain.blog.service;


import dwuthk.search.domain.blog.model.KeywordSearchCountDTO;
import dwuthk.search.external.api.search.BlogSearchExternalConnectService;
import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.api.search.common.model.SearchSort;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BlogSearchService {

    BlogSearchResult searchBlog(SearchBlogParams keyword);


    List<KeywordSearchCountDTO> getMostSearchedKeywords(KeywordStatisticsConditionParams condition, Pageable pageable);

    @Value
    @Builder
    class KeywordStatisticsConditionParams {
        LocalDateTime from;
        LocalDateTime to;
    }


    @Value
    @Builder
    class SearchBlogParams {
        String keyword;
        Integer pageSize;
        Integer page;
        SearchSort sort;

        BlogSearchExternalConnectService.SearchBlogExternalParams convertToExternalParams() {
            return BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                    .keyword(keyword)
                    .pageSize(pageSize)
                    .page(page)
                    .sort(sort)
                    .build();
        }
    }

}
