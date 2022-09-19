package dwuthk.search.domain.blog.service;


import dwuthk.search.external.search.SearchBlogExternalConnectService;
import dwuthk.search.external.search.common.model.BlogSearchResult;
import dwuthk.search.external.search.common.model.ExternalSearchService;
import dwuthk.search.external.search.common.model.SearchSort;
import lombok.Builder;
import lombok.Value;

public interface SearchBlogService {

    BlogSearchResult searchBlog(SearchBlogParams keyword);

    @Value
    @Builder
    class SearchBlogParams {
        String keyword;
        Integer pageSize;
        Integer page;
        SearchSort sort;

        SearchBlogExternalConnectService.SearchBlogExternalParams convertToExternalParams() {
            return SearchBlogExternalConnectService.SearchBlogExternalParams.builder()
                    .keyword(keyword)
                    .pageSize(pageSize)
                    .page(page)
                    .sort(sort)
                    .firstTryService(ExternalSearchService.NAVER)
                    .build();
        }
    }

}
