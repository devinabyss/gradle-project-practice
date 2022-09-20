package dwuthk.search.external.event.model.entity;

import dwuthk.search.external.event.CustomEvent;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "publish_missed_event", indexes = {
        @Index(name = "publish_missed_event_regstered_at", columnList = "registeredAt")
})
public class PublishMissedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType;

    private String jsonStr;

    private LocalDateTime publishedAt;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @Builder
    public PublishMissedEvent(CustomEvent event, String json) {
        this.eventType = event.getEventType();
        this.publishedAt = event.getPublishedAt();
        this.jsonStr = json;
    }
}
