package dwuthk.search.external.api.search.kakao;

import dwuthk.search.common.exception.CustomException;
import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.api.search.common.model.ExternalSearchService;
import dwuthk.search.external.api.search.common.model.SearchSort;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@DisplayName("통합 - 카카오 검색 API 연동")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KaKaoSearchApiClientIntegrationTest {


    @Autowired
    private KaKaoSearchApiClient kaKaoSearchApiClient;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private RetryRegistry retryRegistry;


    @Order(1)
    @Test
    @DisplayName("블로그 검색")
    public void test() {

        KakaoBlogSearchResult result = kaKaoSearchApiClient.searchBlog("유재석",
                SearchSort.ACCURACY.name().toLowerCase(),
                null,
                null
        );

        log.info("## Kakao Search Result : {}", result);
        Assertions.assertEquals(ExternalSearchService.KAKAO, result.getSearchSource());
        Assertions.assertNotNull(result.isContinuable());
        Assertions.assertNotNull(result.getPageableCount());
        Assertions.assertNotNull(result.getTotalCount());
        for (BlogSearchResult.BlogSubjectInfo subjectInfo : result.getSubjects()) {
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getSubjectTitle()));
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getSubjectUrl()));
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getBlogName()));
            Assertions.assertNotNull(subjectInfo.getPostDatetime());
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getSummary()));
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getThumbnailUrl()));
        }
    }


    @Order(2)
    @Test
    @DisplayName("리트라이 설정 확인")
    public void checkRetryConfiguration() {
        Assertions.assertEquals(3, retryRegistry.retry("kakaoSearchBlog").getRetryConfig().getMaxAttempts());
    }

    @Order(3)
    @Test
    @DisplayName("서킷브레이커 동작 확인")
    public void testCircuitBreakerOpenedThrowsScenario() {

        circuitBreakerRegistry.circuitBreaker("kakaoSearchBlog").transitionToOpenState();

        // 써킷브레이커는 CallNotPermittedException 을 Throw 하지만, retryFallback 에 의해 한번 더 감싸져 CustomException 으로 전환
        Assertions.assertThrows(CustomException.class, () -> {
            try {
                KakaoBlogSearchResult result = kaKaoSearchApiClient.searchBlog("유재석",
                        SearchSort.ACCURACY.name().toLowerCase(),
                        null,
                        null
                );
            } catch (Exception e) {
                log.info("## Exception : {} - {}", e.getClass().getCanonicalName(), e.getMessage(), e.getCause());
                log.info("## Cause : {} - {}", e.getCause().getClass().getCanonicalName(), e.getCause().getMessage());
                Assertions.assertTrue(e.getCause() instanceof CallNotPermittedException);
                throw e;
            }
        });
    }


}