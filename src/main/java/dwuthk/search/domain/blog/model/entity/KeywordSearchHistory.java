package dwuthk.search.domain.blog.model.entity;


import dwuthk.search.external.event.model.KeywordSearchedEvent;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "keyword_search_history", indexes = {
        @Index(name = "keyword_idx", columnList = "keyword, searchedAt")
})
public class KeywordSearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    private LocalDateTime searchedAt;


    @Builder
    public KeywordSearchHistory(KeywordSearchedEvent event) {
        this.keyword = event.keyword();
        this.searchedAt = event.searchedAt();
    }

}
