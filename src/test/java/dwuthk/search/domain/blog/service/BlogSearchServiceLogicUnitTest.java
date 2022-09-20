package dwuthk.search.domain.blog.service;

import dwuthk.search.domain.blog.model.entity.repository.KeywordSearchHistoryRepository;
import dwuthk.search.external.api.search.BlogSearchExternalConnectService;
import dwuthk.search.external.api.search.common.model.SearchSort;
import dwuthk.search.external.event.CustomEventPublisher;
import dwuthk.search.external.event.model.KeywordSearchedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@DisplayName("유닛 - 블로그 검색 서비스 로직")
@ExtendWith({MockitoExtension.class})
class BlogSearchServiceLogicUnitTest {


    @InjectMocks
    private BlogSearchServiceImpl service;

    @Mock
    private CustomEventPublisher customEventPublisher;

    @Spy
    private BlogSearchExternalConnectService externalConnectService;

    @Mock
    private KeywordSearchHistoryRepository keywordSearchHistoryRepository;


    @Test
    @DisplayName("검색")
    public void testSearchBlog() {

        BlogSearchService.SearchBlogParams params = BlogSearchService.SearchBlogParams.builder()
                .sort(SearchSort.ACCURACY)
                .keyword("TEST")
                .build();

        service.searchBlog(params);

        Mockito.verify(customEventPublisher, Mockito.times(1))
                .publish(Mockito.refEq(new KeywordSearchedEvent(params.getKeyword(), LocalDateTime.now()), "searchedAt"));

        Mockito.verify(externalConnectService, Mockito.times(1))
                .searchBlogExternal(Mockito.eq(params.convertToExternalParams()));

    }

    @Test
    @DisplayName("키워드 순위 질의")
    public void testMostSearchedKeywords() {

        int favoriteDefaultDays = 7;

        ReflectionTestUtils.setField(service, "favoriteDefaultDays", favoriteDefaultDays);

        BlogSearchService.KeywordStatisticsConditionParams params = BlogSearchService.KeywordStatisticsConditionParams.builder()
                .build();

        Pageable pageable = PageRequest.of(1, 10);

        service.getMostSearchedKeywords(params, pageable);

        Mockito.verify(keywordSearchHistoryRepository, Mockito.times(1))
                .findMostSearchedKeyword(
                        Mockito.refEq(
                                BlogSearchService.KeywordStatisticsConditionParams.builder()
                                        .from(LocalDateTime.now().minusDays(favoriteDefaultDays).truncatedTo(ChronoUnit.DAYS))
                                        .to(LocalDateTime.now())
                                        .build(), "to"
                        ),
                        Mockito.eq(pageable)
                );
    }


}
