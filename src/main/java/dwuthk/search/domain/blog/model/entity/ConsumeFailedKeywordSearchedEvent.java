package dwuthk.search.domain.blog.model.entity;

import dwuthk.search.external.event.model.KeywordSearchedEvent;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "consume_failed_keyword_search_event", indexes = {
        @Index(name = "consume_failed_keyword_search_event_registerd_at", columnList = "registeredAt")
})
public class ConsumeFailedKeywordSearchedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    private LocalDateTime searchedAt;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @Builder
    public ConsumeFailedKeywordSearchedEvent(KeywordSearchedEvent event) {
        this.keyword = event.keyword();
        this.searchedAt = event.searchedAt();
    }
}
