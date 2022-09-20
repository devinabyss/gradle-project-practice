package dwuthk.search.domain.blog.service;


import dwuthk.search.domain.blog.model.entity.ConsumeFailedKeywordSearchedEvent;
import dwuthk.search.domain.blog.model.entity.KeywordSearchHistory;
import dwuthk.search.domain.blog.model.entity.repository.ConsumeFailedKeywordSearchedEventRepository;
import dwuthk.search.domain.blog.model.entity.repository.KeywordSearchHistoryRepository;
import dwuthk.search.external.event.model.KeywordSearchedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeywordSearchEventListener {


    private final KeywordSearchHistoryRepository repository;

    private final ConsumeFailedKeywordSearchedEventRepository failedRepository;

    @Async("eventProcessExecutor")
    @EventListener(KeywordSearchedEvent.class)
    @Transactional
    public void receiveSearchedKeywordEvent(KeywordSearchedEvent event) {

        KeywordSearchHistory history = KeywordSearchHistory.builder()
                .event(event)
                .build();
        try {
            repository.save(history);
        } catch (Exception e) {
            failedRepository.save(ConsumeFailedKeywordSearchedEvent.builder().event(event).build());
        }
    }
}
