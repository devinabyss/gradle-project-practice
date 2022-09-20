package dwuthk.search.external.api.search.naver;


import dwuthk.search.external.api.search.SearchClient;
import dwuthk.search.external.api.search.common.model.ExternalSearchService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverSearch", url = "${external.naver-search.url}", configuration = NaverSearchApiConfiguration.class)
public interface NaverSearchApiClient extends SearchClient {


    @GetMapping(path = "/blog.json")
    @CircuitBreaker(name = "naverSearchBlog", fallbackMethod = "circuitBreakFallback")
    @Retry(name = "naverSearchBlog", fallbackMethod = "retryFallback")
    @Cacheable(value = "naverBlogSearchResult", key = "{#keyword+ '|' + #sort+ '|' + #page+ '|' +#pageSize}")
    NaverBlogSearchResult searchBlog(
            @RequestParam(value = "query") String keyword,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "start") Integer page,
            @RequestParam(value = "display") Integer pageSize
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
