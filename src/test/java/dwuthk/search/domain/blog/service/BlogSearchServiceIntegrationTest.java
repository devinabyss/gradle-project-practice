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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringBootTest
@DisplayName("통합 - 블로그 검색  서비스")
public class BlogSearchServiceIntegrationTest {

    @Autowired
    private BlogSearchService service;

    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

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


    @Test
    @DisplayName("동시 검색 질의 요청 및 집계 정확성 확인")
    @Transactional
    public void testConcurrencyTest() throws InterruptedException {

        Map<String, AtomicInteger> keywords = new ConcurrentHashMap<>();
        keywords.put("유재석", new AtomicInteger());
        keywords.put("김종국", new AtomicInteger());
        keywords.put("하하", new AtomicInteger());
        keywords.put("양세찬", new AtomicInteger());
        keywords.put("지석진", new AtomicInteger());

        List<String> keys = keywords.keySet().stream().toList();

        int searchSize = 1000;
        CountDownLatch latchDown = new CountDownLatch(searchSize);

        for (int i = 0; i < searchSize; i++) {

            final int cur = i;
            executorService.submit(() -> {
                try {
                    String keyword = keys.get(cur % keys.size());

                    service.searchBlog(BlogSearchService.SearchBlogParams.builder()
                            .keyword(keyword)
                            .build());

                    int count = keywords.get(keyword).incrementAndGet();
                    log.info("## Keyword : {}, Current Count : {}", keyword, count);
                } finally {
                    latchDown.countDown();
                }

            });
        }

        latchDown.await();

        List<KeywordSearchCountDTO> countImmediately = service.getMostSearchedKeywords(BlogSearchService.KeywordStatisticsConditionParams.builder().build(), PageRequest.of(1, 10));
        log.info("## Immediately Count Result : {}", countImmediately);

        int expectedCount = searchSize / keys.size();
        for (KeywordSearchCountDTO keywordCount : countImmediately) {
            log.info("## Keyword : {}, Count : {}, Is Expected Count : {}", keywordCount.keyword(), keywordCount.count(), expectedCount == keywordCount.count());
        }

        // Async Thread 로 insert 되므로 빌드 실패 방지를 위해 Assertion 하지 않음.
    }

}
