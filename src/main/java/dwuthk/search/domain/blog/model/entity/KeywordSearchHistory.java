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


    @Column(length = 6)
    private String yearMonth;


    @Column(name = "`day`")
    private Integer day;


    @Column(name = "`hour`")
    private Integer hour;

    private LocalDateTime searchedAt;


    @Builder
    public KeywordSearchHistory(KeywordSearchedEvent event) {
        this.keyword = event.keyword();
        this.yearMonth = event.searchedAt().getYear() + String.format("%02d", event.searchedAt().getMonthValue());
        this.day = event.searchedAt().getDayOfMonth();
        this.hour = event.searchedAt().getHour();
        this.searchedAt = event.searchedAt();
    }

}
