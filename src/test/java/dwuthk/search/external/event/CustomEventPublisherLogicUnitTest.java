package dwuthk.search.external.event;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dwuthk.search.external.event.model.KeywordSearchedEvent;
import dwuthk.search.external.event.model.entity.PublishMissedEvent;
import dwuthk.search.external.event.model.entity.repository.PublishMissedEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

@DisplayName("유닛 - 블로그 검색 외부 커넥션 서비스 로직")
@ExtendWith({MockitoExtension.class})
class CustomEventPublisherLogicUnitTest {

    @InjectMocks
    private CustomEventPublisher publisher;


    @Mock
    private ObjectMapper objectMapper;
    @Spy
    private PublishMissedEventRepository publishMissedEventRepository;

    @Spy
    private ApplicationEventPublisher applicationEventPublisher;


    @Test
    @DisplayName("이벤트 발행 성공")
    public void testPublishEventSuccess() throws JsonProcessingException {

        KeywordSearchedEvent keywordSearchedEvent = new KeywordSearchedEvent("dummy", LocalDateTime.now());

        publisher.publish(keywordSearchedEvent);

        Mockito.verify(applicationEventPublisher, Mockito.times(1)).publishEvent(keywordSearchedEvent);
        Mockito.verify(objectMapper, Mockito.never()).writeValueAsString(keywordSearchedEvent);
        Mockito.verify(publishMissedEventRepository, Mockito.never()).save(PublishMissedEvent.builder().event(keywordSearchedEvent).json("").build());
    }


    @Test
    @DisplayName("이벤트 발행 실패")
    public void testPublishEventFailed() throws JsonProcessingException {

        KeywordSearchedEvent keywordSearchedEvent = new KeywordSearchedEvent("dummy", LocalDateTime.now());
        PublishMissedEvent forTestEntity = new PublishMissedEvent(keywordSearchedEvent, "{}");

        Mockito.doThrow(new RuntimeException()).when(applicationEventPublisher).publishEvent(keywordSearchedEvent);
        Mockito.when(objectMapper.writeValueAsString(keywordSearchedEvent)).thenReturn("{}");

        //Mockito.doReturn() when(publishMissedEventRepository.save()).th will()

        publisher.publish(keywordSearchedEvent);

        Mockito.verify(applicationEventPublisher, Mockito.times(1)).publishEvent(keywordSearchedEvent);
        Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(keywordSearchedEvent);
        Mockito.verify(publishMissedEventRepository, Mockito.times(1)).save(Mockito.refEq(forTestEntity));
    }

    @Test
    @DisplayName("이벤트 발행 실패 JSON 직렬화 실패")
    public void testPublishEventFailedSerializeFail() throws JsonProcessingException {

        KeywordSearchedEvent keywordSearchedEvent = new KeywordSearchedEvent("dummy", LocalDateTime.now());
        PublishMissedEvent forTestEntity = new PublishMissedEvent(keywordSearchedEvent, "{}");

        Mockito.doThrow(new RuntimeException()).when(applicationEventPublisher).publishEvent(keywordSearchedEvent);
        Mockito.when(objectMapper.writeValueAsString(keywordSearchedEvent)).thenThrow(new RuntimeException());

        publisher.publish(keywordSearchedEvent);

        Mockito.verify(applicationEventPublisher, Mockito.times(1)).publishEvent(keywordSearchedEvent);
        Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(keywordSearchedEvent);
        Mockito.verify(publishMissedEventRepository, Mockito.never()).save(Mockito.refEq(forTestEntity));
    }


}