package dwuthk.search.domain.blog.model.entity.repository;


import dwuthk.search.config.PersistenceConfig;
import dwuthk.search.domain.blog.model.entity.ConsumeFailedKeywordSearchedEvent;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(PersistenceConfig.class)
@DisplayName("통합 - 저장소 - 키워드 검색 이벤트 소비 실패")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConsumeFailedKeywordSearchedEventRepositoryIntegrationTest {

    @Autowired
    private ConsumeFailedKeywordSearchedEventRepository repository;


    @Test
    public void testInsert() {

        ConsumeFailedKeywordSearchedEvent entity = ConsumeFailedKeywordSearchedEvent.builder()
                .event(new KeywordSearchedEvent("유재석", LocalDateTime.now()))
                .build();

        repository.save(entity);
        log.info("## Entity : {}", entity);

        List<ConsumeFailedKeywordSearchedEvent> list = repository.findAll();
        log.info("## Entities : {}", list);

        Assertions.assertNotNull(entity.getId());
        Assertions.assertEquals(1, list.size());
    }

}

