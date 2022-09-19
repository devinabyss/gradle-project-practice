package dwuthk.search.external.search.kakao;


import com.fasterxml.jackson.annotation.JsonProperty;
import dwuthk.search.external.search.SearchClient;
import dwuthk.search.external.search.common.model.ExternalSearchService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "kakaoSearch",
        url = "${external.kakao-search.url}",
        configuration = KakaoSearchApiConfiguration.class

)
public interface KaKaoSearchApiClient extends SearchClient {

    @CircuitBreaker(name = "kakaoSearchBlog", fallbackMethod = "circuitBreakFallback")
    @Retry(name = "kakaoSearchBlog", fallbackMethod = "retryFallback")
    @GetMapping(path = "/blog")
    KakaoBlogSearchResult searchBlog(
            @RequestParam("query") String keyword,
            @RequestParam("sort") String sort,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer pageSize
    );


    record Meta(
            @JsonProperty("total_count")
            Integer totalCount,
            @JsonProperty("pageable_count")
            Integer pageableCount,
            @JsonProperty("is_end")
            Boolean isEnd
    ) {
    }


    default KakaoBlogSearchResult retryFallback(Throwable e) throws Throwable {
        throw defaultRetryFallbackException(e);
    }

    default KakaoBlogSearchResult circuitBreakFallback(Throwable e) throws Throwable {
        throw defaultCircuitBreakFallbackException(e);
    }

    @Override
    default Integer getDefaultOrder() {
        return 1;
    }

    @Override
    default ExternalSearchService getExternalService() {
        return ExternalSearchService.KAKAO;
    }
//
//    @Override
//    default int compareTo(SearchClient another) {
//        return getDefaultOrder().compareTo(another.getDefaultOrder());
//    }
}
