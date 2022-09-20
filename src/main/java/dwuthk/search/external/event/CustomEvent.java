package dwuthk.search.external.event;

import java.time.LocalDateTime;

public interface CustomEvent {

    default String getEventType() {
        return this.getClass().getSimpleName();
    }

    LocalDateTime getPublishedAt();

}
