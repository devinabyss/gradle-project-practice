package dwuthk.search.domain.blog.controller;


import dwuthk.search.domain.blog.model.KeywordSearchCountDTO;
import dwuthk.search.domain.blog.model.entity.repository.KeywordSearchHistoryRepository;
import dwuthk.search.domain.blog.service.BlogSearchService;
import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.api.search.common.model.SearchSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("blog")
public class BlogSearchController {

    private final BlogSearchService blogSearchService;

    private final KeywordSearchHistoryRepository repository;

    @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@Validated BlogSearchRequestDTO searchRequestDTO) {

        BlogSearchResult result = blogSearchService.searchBlog(searchRequestDTO.convertToServiceParam());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping(value = "favoriteKeywords", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> statistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDateTime to
    ) {

        List<KeywordSearchCountDTO> list = blogSearchService.getMostSearchedKeywords(BlogSearchService.KeywordStatisticsConditionParams.builder()
                .from(from).to(to).build(), PageRequest.of(1, 10));

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    private record BlogSearchRequestDTO(
            @NotEmpty(message = "keyword 는 반드시 입력되어야 합니다.") String keyword,
            @Max(value = 50, message = "최대 허용값은 50 입니다.") Integer page,
            @Max(value = 50, message = "최대 허용값은 50 입니다.") Integer pageSize,
            SearchSort sort) {
        public BlogSearchService.SearchBlogParams convertToServiceParam() {
            return BlogSearchService.SearchBlogParams.builder()
                    .keyword(keyword())
                    .page(page())
                    .pageSize(pageSize())
                    .sort(sort())
                    .build();
        }
    }

}
