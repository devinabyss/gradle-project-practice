package dwuthk.search.external.api.search.naver;

import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.api.search.common.model.ExternalSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        for (BlogSearchResult.BlogSubjectInfo subjectInfo : result.getSubjects()) {
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getSubjectTitle()));
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getSubjectUrl()));
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getBlogName()));
            Assertions.assertNotNull(subjectInfo.getPostDatetime());
            Assertions.assertTrue(StringUtils.isNotEmpty(subjectInfo.getSummary()));
            Assertions.assertTrue(StringUtils.isEmpty(subjectInfo.getThumbnailUrl()));
        }
    }

}