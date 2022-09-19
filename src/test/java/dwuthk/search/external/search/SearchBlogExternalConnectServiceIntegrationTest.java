package dwuthk.search.external.search;

import dwuthk.search.external.search.common.model.BlogSearchResult;
import dwuthk.search.external.search.common.model.ExternalSearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@DisplayName("통합 - 블로그 검색 외부 커넥션 서비스")
class SearchBlogExternalConnectServiceIntegrationTest {

    @Autowired
    private SearchBlogExternalConnectService service;


    @Test
    @DisplayName("기본 검색 클라이언트 순서")
    public void testDefaultSearchService() {

        SearchBlogExternalConnectService.SearchBlogExternalParams params = SearchBlogExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("유재석")
                .build();

        BlogSearchResult result = service.searchBlogExternal(params);
        Assertions.assertEquals(ExternalSearchService.KAKAO, result.getSearchSource());

    }

    @Test
    @DisplayName("지정 검색 클라언트 순서 - 네이버")
    public void testAppointSearchService() {

        SearchBlogExternalConnectService.SearchBlogExternalParams params = SearchBlogExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("유재석")
                .firstTryService(ExternalSearchService.NAVER)
                .build();

        BlogSearchResult result = service.searchBlogExternal(params);
        Assertions.assertEquals(ExternalSearchService.NAVER, result.getSearchSource());

    }
}