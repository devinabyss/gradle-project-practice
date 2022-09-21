package dwuthk.search.external.api.search;

import dwuthk.search.common.exception.CustomException;
import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.api.search.common.model.ExternalSearchService;
import dwuthk.search.external.api.search.kakao.KaKaoSearchApiClient;
import dwuthk.search.external.api.search.kakao.KakaoBlogSearchResult;
import dwuthk.search.external.api.search.kakao.model.Meta;
import dwuthk.search.external.api.search.naver.NaverBlogSearchResult;
import dwuthk.search.external.api.search.naver.NaverSearchApiClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@DisplayName("유닛 - 블로그 검색 외부 커넥션 서비스 로직")
@ExtendWith({MockitoExtension.class})
class BlogSearchExternalConnectServiceLogicUnitTest {

    @InjectMocks
    BlogSearchExternalConnectServiceImpl service;

    @Spy
    KaKaoSearchApiClient kaKaoSearchApiClient;

    @Spy
    NaverSearchApiClient naverSearchApiClient;


    @BeforeEach
    public void afterServicePropertiesSet() {
        service.afterPropertiesSet();
    }

    @Test
    public void test(){
        LocalDateTime now = LocalDateTime.now();
        String a = now.getYear() + String.format("%02d", now.getMonthValue());
        log.info("## A: {}", a);

    }

    @Test
    @DisplayName("SearchClient 기본 호출 순서")
    public void testDefaultClientCallOrder() {

        BlogSearchExternalConnectService.SearchBlogExternalParams params = BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("유재석")
                .build();

        Mockito.when(kaKaoSearchApiClient.searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.KAKAO), params.getPage(), params.getPageSize()))
                .thenReturn(new KakaoBlogSearchResult(new ArrayList<>(), new Meta(1, 1, true)));

        BlogSearchResult result = service.searchBlogExternal(params);

        Mockito.verify(kaKaoSearchApiClient, Mockito.times(1))
                .searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.KAKAO), params.getPage(), params.getPageSize());
        Mockito.verify(naverSearchApiClient, Mockito.never())
                .searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.NAVER), params.getPage(), params.getPageSize());

        Assertions.assertEquals(ExternalSearchService.KAKAO, result.getSearchSource());
    }

    @Test
    @DisplayName("SearchClient 지정 호출 순서")
    public void testAppointClientCallOrder() {

        BlogSearchExternalConnectService.SearchBlogExternalParams params = BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("유재석")
                .firstTryService(ExternalSearchService.NAVER)
                .build();

        Mockito.when(naverSearchApiClient.searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.NAVER), params.getPage(), params.getPageSize()))
                .thenReturn(new NaverBlogSearchResult("date", 1, 1, 1, new ArrayList<>()));

        BlogSearchResult result = service.searchBlogExternal(params);

        Mockito.verify(kaKaoSearchApiClient, Mockito.never())
                .searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.KAKAO), params.getPage(), params.getPageSize());
        Mockito.verify(naverSearchApiClient, Mockito.times(1))
                .searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.NAVER), params.getPage(), params.getPageSize());

        Assertions.assertEquals(ExternalSearchService.NAVER, result.getSearchSource());
    }

    @Test
    @DisplayName("SearchClient 존재하지 않는 클라이언트")
    public void testNotExistsClientCallOrder() {

        Assertions.assertThrows(CustomException.class, () -> {
            BlogSearchExternalConnectService.SearchBlogExternalParams params = BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                    .keyword("유재석")
                    .firstTryService(ExternalSearchService.ETC)
                    .build();

            BlogSearchResult result = service.searchBlogExternal(params);
        });

    }

    @Test
    @DisplayName("SearchClient 순환 클라이언트 대체")
    public void testIteratingClientWhenOrderingClientFails() {

        BlogSearchExternalConnectService.SearchBlogExternalParams params = BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("유재석")
                .build();

        Mockito.when(kaKaoSearchApiClient.searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.KAKAO), params.getPage(), params.getPageSize()))
                .thenThrow(new RuntimeException());

        Mockito.when(naverSearchApiClient.searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.NAVER), params.getPage(), params.getPageSize()))
                .thenReturn(new NaverBlogSearchResult("date", 1, 1, 1, new ArrayList<>()));

        BlogSearchResult result = service.searchBlogExternal(params);

        Mockito.verify(kaKaoSearchApiClient, Mockito.times(1))
                .searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.KAKAO), params.getPage(), params.getPageSize());
        Mockito.verify(naverSearchApiClient, Mockito.times(1))
                .searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.NAVER), params.getPage(), params.getPageSize());

        Assertions.assertEquals(ExternalSearchService.NAVER, result.getSearchSource());
    }

    @Test
    @DisplayName("SearchClient 모든 클라이언트 실패")
    public void testIteratingClientAllFails() {

        Assertions.assertThrows(CustomException.class, () -> {

            BlogSearchExternalConnectService.SearchBlogExternalParams params = BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                    .keyword("유재석")
                    .build();

            Mockito.when(kaKaoSearchApiClient.searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.KAKAO), params.getPage(), params.getPageSize()))
                    .thenThrow(new RuntimeException());

            Mockito.when(naverSearchApiClient.searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.NAVER), params.getPage(), params.getPageSize()))
                    .thenThrow(new RuntimeException());

            BlogSearchResult result = service.searchBlogExternal(params);

            Mockito.verify(kaKaoSearchApiClient, Mockito.times(1))
                    .searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.KAKAO), params.getPage(), params.getPageSize());
            Mockito.verify(naverSearchApiClient, Mockito.times(1))
                    .searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.NAVER), params.getPage(), params.getPageSize());

        });
    }


}