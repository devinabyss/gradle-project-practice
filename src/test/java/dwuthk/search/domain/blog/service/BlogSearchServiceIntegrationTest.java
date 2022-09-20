package dwuthk.search.domain.blog.service;

import dwuthk.search.domain.blog.model.KeywordSearchCountDTO;
import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.api.search.common.model.ExternalSearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
@DisplayName("통합 - 블로그 검색  서비스")
public class BlogSearchServiceIntegrationTest {

    @Autowired
    private BlogSearchService service;


    @Test
    @DisplayName("기본 검색 클라이언트 순서")
    public void testSearchService() {

        BlogSearchService.SearchBlogParams params = BlogSearchService.SearchBlogParams.builder()
                .keyword("아무거나")
                .build();

        BlogSearchResult result = service.searchBlog(params);
        Assertions.assertEquals(ExternalSearchService.KAKAO, result.getSearchSource());
        Assertions.assertEquals(10, result.getSubjects().size());

    }

    @Test
    @DisplayName("검색어 순위")
    public void testGetMostSearchedKeywords() throws InterruptedException {

        BlogSearchService.SearchBlogParams params = BlogSearchService.SearchBlogParams.builder()
                .keyword("아무거나")
                .build();

        BlogSearchResult result = service.searchBlog(params);
        Assertions.assertEquals(ExternalSearchService.KAKAO, result.getSearchSource());
        Assertions.assertEquals(10, result.getSubjects().size());

        TimeUnit.SECONDS.sleep(1);

        BlogSearchService.KeywordStatisticsConditionParams keywordStatisticsConditionParams = BlogSearchService.KeywordStatisticsConditionParams.builder()
                .build();

        List<KeywordSearchCountDTO> keywords = service.getMostSearchedKeywords(keywordStatisticsConditionParams, PageRequest.of(1, 10));

        Assertions.assertEquals(1, keywords.size());
        Assertions.assertEquals("아무거나", keywords.get(0).keyword());
    }
}
