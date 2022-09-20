package dwuthk.search.domain.blog.service;

import dwuthk.search.domain.blog.model.KeywordSearchCountDTO;
import dwuthk.search.domain.blog.model.entity.repository.KeywordSearchHistoryRepository;
import dwuthk.search.external.api.search.BlogSearchExternalConnectService;
import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.event.CustomEventPublisher;
import dwuthk.search.external.event.model.KeywordSearchedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchServiceImpl implements BlogSearchService {

    @Value("${operation.favoriteDefaultDays}")
    private Integer favoriteDefaultDays;
    private final BlogSearchExternalConnectService externalSearchService;

    private final CustomEventPublisher customEventPublisher;

    private final KeywordSearchHistoryRepository keywordSearchHistoryRepository;

    @Override
    public BlogSearchResult searchBlog(SearchBlogParams params) {

        customEventPublisher.publish(new KeywordSearchedEvent(params.getKeyword(), LocalDateTime.now()));

        BlogSearchResult result = externalSearchService
                .searchBlogExternal(params.convertToExternalParams());

        return result;
    }


    @Override
    public List<KeywordSearchCountDTO> getMostSearchedKeywords(KeywordStatisticsCondition condition, Pageable pageable) {
        return keywordSearchHistoryRepository.findMostSearchedKeyword(KeywordStatisticsCondition.builder()
                .from(Objects.nonNull(condition.getFrom()) ? condition.getFrom() : LocalDateTime.now().minusMinutes(favoriteDefaultDays).truncatedTo(ChronoUnit.DAYS))
                .to(Objects.nonNull(condition.getTo()) ? condition.getTo() : LocalDateTime.now())
                .build(), pageable);
    }

}
