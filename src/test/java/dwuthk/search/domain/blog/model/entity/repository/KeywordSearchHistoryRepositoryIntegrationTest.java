package dwuthk.search.domain.blog.model.entity.repository;

import dwuthk.search.config.PersistenceConfig;
import dwuthk.search.domain.blog.model.KeywordSearchCountDTO;
import dwuthk.search.domain.blog.model.entity.KeywordSearchHistory;
import dwuthk.search.domain.blog.model.entity.repository.KeywordSearchHistoryRepository;
import dwuthk.search.domain.blog.service.BlogSearchService;
import dwuthk.search.external.event.model.KeywordSearchedEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(PersistenceConfig.class)
@DisplayName("통합 - 저장소 - 키워드 검색 이력")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class KeywordSearchHistoryRepositoryIntegrationTest {


    @Autowired
    private KeywordSearchHistoryRepository repository;


    @Test
    public void testInsert() {

        KeywordSearchHistory newHistory = KeywordSearchHistory.builder()
                .event(new KeywordSearchedEvent("유재석", LocalDateTime.now()))
                .build();

        repository.save(newHistory);
        log.info("## History : {}", newHistory);

        List<KeywordSearchHistory> searchKeywordHistories = repository.findAll();
        log.info("## Entities : {}", searchKeywordHistories);

        Assertions.assertNotNull(newHistory.getId());
        Assertions.assertEquals(1, searchKeywordHistories.size());
    }


    @Test
    public void testStatisticsCount() {

        KeywordSearchHistory newHistory = KeywordSearchHistory.builder()
                .event(new KeywordSearchedEvent("유재석", LocalDateTime.now()))
                .build();

        repository.save(newHistory);
        log.info("## History : {}", newHistory);


        List<KeywordSearchCountDTO> list = repository.findMostSearchedKeyword(BlogSearchService.KeywordStatisticsConditionParams.builder()
                .from(LocalDateTime.now().minusDays(3))
                .build(), PageRequest.of(1, 10));

        log.info("## List : {}", list);

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(1, list.get(0).count());
        Assertions.assertEquals("유재석", list.get(0).keyword());

    }

}