package dwuthk.search.external.event.model;

import dwuthk.search.external.event.CustomEvent;

import java.time.LocalDateTime;


public record KeywordSearchedEvent(String keyword, LocalDateTime searchedAt) implements CustomEvent {

    @Override
    public LocalDateTime getPublishedAt() {
        return searchedAt();
    }
}
