package dwuthk.search.external.api.search;

import dwuthk.search.external.api.DefaultClient;
import dwuthk.search.external.api.search.common.model.BlogSearchResult;
import dwuthk.search.external.api.search.common.model.ExternalSearchService;

public interface SearchClient extends DefaultClient {

    Integer getDefaultOrder();

    ExternalSearchService getExternalService();

    BlogSearchResult searchBlog(String keyword, String sort, Integer page, Integer pageSize);

}
