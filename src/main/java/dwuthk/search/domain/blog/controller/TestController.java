package dwuthk.search.domain.blog.controller;


import dwuthk.search.domain.blog.service.SearchBlogService;
import dwuthk.search.external.search.common.model.BlogSearchResult;
import dwuthk.search.external.search.common.model.SearchSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final SearchBlogService searchBlogService;

    @GetMapping("test")
    public ResponseEntity<?> test() {

        BlogSearchResult result = searchBlogService.searchBlog(
                SearchBlogService.SearchBlogParams.builder()
                        .keyword("김종국")
                        .pageSize(50)
                        .page(5)
                        .sort(SearchSort.RECENCY)

                        .build()
        );

        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
