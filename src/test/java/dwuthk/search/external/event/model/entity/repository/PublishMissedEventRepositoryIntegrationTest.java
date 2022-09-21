package dwuthk.search.external.event.model.entity.repository;

import dwuthk.search.config.PersistenceConfig;
import dwuthk.search.external.event.model.KeywordSearchedEvent;
import dwuthk.search.external.event.model.entity.PublishMissedEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
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
@DisplayName("통합 - 저장소 - 발행 실패 이벤트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublishMissedEventRepositoryIntegrationTest {

    @Autowired
    private PublishMissedEventRepository repository;

    @Order(1)
    @Test
    public void testInsert() {

        KeywordSearchedEvent source = new KeywordSearchedEvent("test", LocalDateTime.now());
        PublishMissedEvent entity = PublishMissedEvent.builder().event(source).json("{}").build();

        repository.save(entity);

        log.info("## PublishMissedEvent : {}", entity);

        List<PublishMissedEvent> histories = repository.findAll();
        log.info("## Entities : {}", histories);

        Assertions.assertNotNull(entity.getId());

    }

}