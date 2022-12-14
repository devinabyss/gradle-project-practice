package dwuthk.search.external.api.search;

import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.api.search.common.model.ExternalSearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@DisplayName("통합 - 블로그 검색 외부 커넥션 서비스")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BlogSearchExternalConnectServiceIntegrationTest {

    @Autowired
    private BlogSearchExternalConnectService service;



    @Order(1)
    @Test
    @DisplayName("기본 검색 클라이언트 순서")
    public void testDefaultSearchService() {

        BlogSearchExternalConnectService.SearchBlogExternalParams params = BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("유재석")
                .build();

        BlogSearchResult result = service.searchBlogExternal(params);
        Assertions.assertEquals(ExternalSearchService.KAKAO, result.getSearchSource());

    }

    @Test
    @Order(2)
    @DisplayName("지정 검색 클라언트 순서 - 네이버")
    public void testAppointSearchService() {

        BlogSearchExternalConnectService.SearchBlogExternalParams params = BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("유재석")
                .firstTryService(ExternalSearchService.NAVER)
                .build();

        BlogSearchResult result = service.searchBlogExternal(params);
        Assertions.assertEquals(ExternalSearchService.NAVER, result.getSearchSource());

    }
}