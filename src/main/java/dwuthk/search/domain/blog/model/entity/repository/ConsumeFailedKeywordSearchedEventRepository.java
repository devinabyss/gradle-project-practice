package dwuthk.search.domain.blog.model.entity.repository;

import dwuthk.search.domain.blog.model.entity.ConsumeFailedKeywordSearchedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumeFailedKeywordSearchedEventRepository extends JpaRepository<ConsumeFailedKeywordSearchedEvent, Long> {
}
