package dwuthk.search.external.search;


import dwuthk.search.external.search.common.model.BlogSearchResult;
import dwuthk.search.external.search.common.model.ExternalSearchService;
import dwuthk.search.external.search.common.model.SearchSort;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

public interface SearchBlogExternalConnectService {

    BlogSearchResult searchBlogExternal(SearchBlogExternalParams params);

    @Builder
    @Value
    class SearchBlogExternalParams {
        String keyword;
        Integer pageSize;
        Integer page;
        SearchSort sort;
        ExternalSearchService firstTryService;

        public String getSuitableSort(ExternalSearchService service) {
            SearchSort defaultSort = Optional.ofNullable(getSort()).orElseGet(() -> SearchSort.ACCURACY);
            return switch (service) {
                case KAKAO -> defaultSort.name().toLowerCase();
                case NAVER -> switch (defaultSort) {
                    case ACCURACY -> "sim";
                    case RECENCY -> "date";
                };
            };
        }
    }

}
