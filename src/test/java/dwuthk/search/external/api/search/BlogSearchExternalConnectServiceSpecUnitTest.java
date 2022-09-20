package dwuthk.search.external.api.search;

import dwuthk.search.external.api.search.common.model.ExternalSearchService;
import dwuthk.search.external.api.search.common.model.SearchSort;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
@DisplayName("유닛 - 블로그 검색 외부 커넥션 서비스 스펙")
class BlogSearchExternalConnectServiceSpecUnitTest {


    @Test
    @DisplayName("기본 소팅 파라미터 파싱")
    public void testDefaultSort() {

        BlogSearchExternalConnectService.SearchBlogExternalParams params = BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("Test")
                .build();


        log.info("## SuitableSort : Naver - {}, Kakao - {}", params.getSuitableSort(ExternalSearchService.NAVER), params.getSuitableSort(ExternalSearchService.KAKAO));


        Assertions.assertEquals("sim", params.getSuitableSort(ExternalSearchService.NAVER));
        Assertions.assertEquals(SearchSort.ACCURACY.name().toLowerCase(), params.getSuitableSort(ExternalSearchService.KAKAO));
    }

    @Test
    @DisplayName("정확도 소팅 파라미터 파싱")
    public void testAccuracySort() {

        BlogSearchExternalConnectService.SearchBlogExternalParams params = BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("Test")
                .sort(SearchSort.ACCURACY)
                .build();

        log.info("## SuitableSort : Naver - {}, Kakao - {}", params.getSuitableSort(ExternalSearchService.NAVER), params.getSuitableSort(ExternalSearchService.KAKAO));

        Assertions.assertEquals("sim", params.getSuitableSort(ExternalSearchService.NAVER));
        Assertions.assertEquals(SearchSort.ACCURACY.name().toLowerCase(), params.getSuitableSort(ExternalSearchService.KAKAO));
    }

    @Test
    @DisplayName("최신순 소팅 파라미터 파싱")
    public void testRecencySort() {

        BlogSearchExternalConnectService.SearchBlogExternalParams params = BlogSearchExternalConnectService.SearchBlogExternalParams.builder()
                .keyword("Test")
                .sort(SearchSort.RECENCY)
                .build();

        log.info("## SuitableSort : Naver - {}, Kakao - {}", params.getSuitableSort(ExternalSearchService.NAVER), params.getSuitableSort(ExternalSearchService.KAKAO));

        Assertions.assertEquals("date", params.getSuitableSort(ExternalSearchService.NAVER));
        Assertions.assertEquals(SearchSort.RECENCY.name().toLowerCase(), params.getSuitableSort(ExternalSearchService.KAKAO));
    }

}