package dwuthk.search.domain.blog.service;

import dwuthk.search.external.search.SearchBlogExternalConnectService;
import dwuthk.search.external.search.common.model.BlogSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchBlogServiceImpl implements SearchBlogService {

    private final SearchBlogExternalConnectService externalSearchService;


    @Override
    public BlogSearchResult searchBlog(SearchBlogParams params) {


        saveSearchTriedKeyword(params.getKeyword());


        BlogSearchResult result = externalSearchService
                .searchBlogExternal(params.convertToExternalParams());


        return result;
    }

    private void saveSearchTriedKeyword(String keyword) {
    }


}
