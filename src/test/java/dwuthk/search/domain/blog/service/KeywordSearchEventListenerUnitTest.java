package dwuthk.search.domain.blog.service;


import dwuthk.search.domain.blog.model.entity.ConsumeFailedKeywordSearchedEvent;
import dwuthk.search.domain.blog.model.entity.KeywordSearchHistory;
import dwuthk.search.domain.blog.model.entity.repository.ConsumeFailedKeywordSearchedEventRepository;
import dwuthk.search.domain.blog.model.entity.repository.KeywordSearchHistoryRepository;
import dwuthk.search.external.event.model.KeywordSearchedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@DisplayName("유닛 - 키워드 질의 이벤트 리스너")
@ExtendWith({MockitoExtension.class})
class KeywordSearchEventListenerUnitTest {

    @InjectMocks
    private KeywordSearchEventListener listener;

    @Mock
    private KeywordSearchHistoryRepository keywordSearchHistoryRepository;

    @Mock
    private ConsumeFailedKeywordSearchedEventRepository consumeFailedKeywordSearchedEventRepository;

    @Test
    @DisplayName("정상 동작")
    public void testSearchHistoryStore() {


        KeywordSearchedEvent event = new KeywordSearchedEvent("test", LocalDateTime.now());

        listener.receiveSearchedKeywordEvent(event);

        Mockito.verify(keywordSearchHistoryRepository, Mockito.times(1))
                .save(Mockito.refEq(KeywordSearchHistory.builder().event(event).build(), "id"));


    }

    @Test
    @DisplayName("저장 실패")
    public void testSearchHistoryStoreFailed() {


        KeywordSearchedEvent event = new KeywordSearchedEvent("test", LocalDateTime.now());

        Mockito.when(keywordSearchHistoryRepository.save(Mockito.any())).thenThrow(new RuntimeException());

        listener.receiveSearchedKeywordEvent(event);

        Mockito.verify(keywordSearchHistoryRepository, Mockito.times(1))
                .save(Mockito.refEq(KeywordSearchHistory.builder().event(event).build(), "id"));

        Mockito.verify(consumeFailedKeywordSearchedEventRepository, Mockito.times(1))
                .save(Mockito.refEq(ConsumeFailedKeywordSearchedEvent.builder().event(event).build(), "id"));

    }

}