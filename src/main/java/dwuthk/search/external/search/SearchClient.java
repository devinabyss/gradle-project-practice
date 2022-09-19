package dwuthk.search.external.search;

import dwuthk.search.external.DefaultClient;
import dwuthk.search.external.search.common.model.BlogSearchResult;
import dwuthk.search.external.search.common.model.ExternalSearchService;

public interface SearchClient extends DefaultClient //, Comparable<SearchClient>
{

    Integer getDefaultOrder();

    ExternalSearchService getExternalService();

    BlogSearchResult searchBlog(String keyword, String sort, Integer page, Integer pageSize);

}
