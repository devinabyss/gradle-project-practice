package dwuthk.search.external.search.naver;

import dwuthk.search.external.search.common.model.ExternalSearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@DisplayName("통합 - 네이버 검색 API 연동")
@SpringBootTest
class NaverSearchApiClientIntegrationTest {
    @Autowired
    private NaverSearchApiClient naverSearchApiClient;


    @Test
    @DisplayName("블로그 검색")
    public void test() {

        NaverBlogSearchResult result = naverSearchApiClient.searchBlog("유재석",
                "sim",
                null,
                null
        );

        log.info("## Naver Search Result : {}", result);
        Assertions.assertEquals(ExternalSearchService.NAVER, result.getSearchSource());
        Assertions.assertNotNull(result.isContinuable());
        Assertions.assertNotNull(result.getPageableCount());
        Assertions.assertNotNull(result.getTotalCount());
    }

}