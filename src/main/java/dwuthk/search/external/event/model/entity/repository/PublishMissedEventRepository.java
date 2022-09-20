package dwuthk.search.external.event.model.entity.repository;

import dwuthk.search.external.event.model.entity.PublishMissedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishMissedEventRepository extends JpaRepository<PublishMissedEvent, Long> {
}
