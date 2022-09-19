package dwuthk.search.external.search.naver;


import dwuthk.search.external.search.SearchClient;
import dwuthk.search.external.search.common.model.ExternalSearchService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverSearch", url = "${external.naver-search.url}", configuration = NaverSearchApiConfiguration.class)
public interface NaverSearchApiClient extends SearchClient {


    @GetMapping(path = "/blog.json")
    @CircuitBreaker(name = "naverSearchBlog", fallbackMethod = "circuitBreakFallback")
    @Retry(name = "naverSearchBlog", fallbackMethod = "retryFallback")
    NaverBlogSearchResult searchBlog(
            @RequestParam("query") String keyword,
            @RequestParam("sort") String sort,
            @RequestParam("start") Integer page,
            @RequestParam("display") Integer pageSize
            //@SpringQueryMap SearchBlogClientQueryParams params
    );

    @Override
    default Integer getDefaultOrder() {
        return 2;
    }

    @Override
    default ExternalSearchService getExternalService() {
        return ExternalSearchService.NAVER;
    }

    default NaverBlogSearchResult retryFallback(Throwable e) throws Throwable {
        throw defaultRetryFallbackException(e);
    }

    default NaverBlogSearchResult circuitBreakFallback(Throwable e) throws Throwable {
        throw defaultCircuitBreakFallbackException(e);
    }

}
