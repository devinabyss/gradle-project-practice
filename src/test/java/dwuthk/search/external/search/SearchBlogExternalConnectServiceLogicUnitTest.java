package dwuthk.search.external.search;

import dwuthk.search.external.search.common.model.BlogSearchResult;
import dwuthk.search.external.search.common.model.ExternalSearchService;
import dwuthk.search.external.search.kakao.KaKaoSearchApiClient;
import dwuthk.search.external.search.kakao.KakaoBlogSearchResult;
import dwuthk.search.external.search.naver.NaverBlogSearchResult;
import dwuthk.search.external.search.naver.NaverSearchApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@DisplayName("유닛 - 블로그 검색 외부 커넥션 서비스 로직")
@ExtendWith({MockitoExtension.class})
class SearchBlogExternalConnectServiceLogicUnitTest {

    @InjectMocks
    SearchBlogExternalConnectServiceImpl service;

    @Spy
    KaKaoSearchApiClient kaKaoSearchApiClient;

    @Spy
    NaverSearchApiClient naverSearchApiClient;


    @BeforeEach
    public void afterServicePropertiesSet() {
        service.afterPropertiesSet();
    }


    @Test
    @DisplayName("SearchClient 기본 호출 순서")
    public void testDefaultClientCallOrder() {

        SearchBlogExternalConnectService.SearchBlogExternalParams params = SearchBlogExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("유재석")
                .build();

        Mockito.when(kaKaoSearchApiClient.searchBlog(params.getKeyword(), params.getSuitableSort(ExternalSearchService.KAKAO), params.getPage(), params.getPageSize()))
                .thenReturn(new KakaoBlogSearchResult(new ArrayList<>(), new KaKaoSearchApiClient.Meta(1, 1, true)));

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

        SearchBlogExternalConnectService.SearchBlogExternalParams params = SearchBlogExternalConnectService.SearchBlogExternalParams.builder()
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
    @DisplayName("SearchClient 순환 클라이언트 대체")
    public void testIteratingClientWhenOrderingClientFails() {

        SearchBlogExternalConnectService.SearchBlogExternalParams params = SearchBlogExternalConnectService.SearchBlogExternalParams.builder()
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

        Assertions.assertThrows(RuntimeException.class, () -> {

            SearchBlogExternalConnectService.SearchBlogExternalParams params = SearchBlogExternalConnectService.SearchBlogExternalParams.builder()
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