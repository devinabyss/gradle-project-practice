package dwuthk.search.domain.blog.model.entity.repository;


import dwuthk.search.domain.blog.model.entity.KeywordSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordSearchHistoryRepository extends JpaRepository<KeywordSearchHistory, Long>, KeywordSearchHistoryRepositoryCustom {

}
