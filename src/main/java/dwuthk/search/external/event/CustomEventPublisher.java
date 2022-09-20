package dwuthk.search.external.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import dwuthk.search.external.event.model.entity.PublishMissedEvent;
import dwuthk.search.external.event.model.entity.repository.PublishMissedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomEventPublisher {

    private final ObjectMapper objectMapper;

    private final PublishMissedEventRepository publishMissedEventRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(CustomEvent event) {
        try {
            applicationEventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.warn("## Event Publish Missed. Save Missed Event. : {}", event);
            try {
                String json = objectMapper.writeValueAsString(event);
                PublishMissedEvent ev = PublishMissedEvent.builder().event(event).json(json).build();
                publishMissedEventRepository.save(ev);
            } catch (Exception ex) {
                log.error("## Missed Publish Event Parsing Failed. : {}", event);
            }
        }
    }
}
