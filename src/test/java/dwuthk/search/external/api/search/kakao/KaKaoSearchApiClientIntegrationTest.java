package dwuthk.search.external.api.search.kakao;

import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.api.search.common.model.ExternalSearchService;
import dwuthk.search.external.api.search.common.model.SearchSort;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        for (BlogSearchResult.BlogSubjectInfo subjectInfo : result.getSubjects()) {
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getSubjectTitle()));
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getSubjectUrl()));
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getBlogName()));
            Assertions.assertNotNull(subjectInfo.getPostDatetime());
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getSummary()));
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getThumbnailUrl()));
        }
    }


}