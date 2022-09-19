package dwuthk.search.external.search.kakao;

import dwuthk.search.external.search.common.model.ExternalSearchService;
import dwuthk.search.external.search.common.model.SearchSort;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@DisplayName("통합 - 카카오 검색 API 연동")
@SpringBootTest
class KaKaoSearchApiClientIntegrationTest {

    @Autowired
    private KaKaoSearchApiClient kaKaoSearchApiClient;


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
    }


}