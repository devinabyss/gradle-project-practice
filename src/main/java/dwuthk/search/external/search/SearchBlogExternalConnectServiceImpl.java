package dwuthk.search.external.search;

import dwuthk.search.external.search.common.model.BlogSearchResult;
import dwuthk.search.external.search.kakao.KaKaoSearchApiClient;
import dwuthk.search.external.search.naver.NaverSearchApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchBlogExternalConnectServiceImpl implements SearchBlogExternalConnectService, InitializingBean {

    private final KaKaoSearchApiClient kaKaoSearchApiClient;

    private final NaverSearchApiClient naverSearchApiClient;

    private final List<SearchClient> searchClients = new ArrayList<>();

    @Override
    public BlogSearchResult searchBlogExternal(SearchBlogExternalParams params) {
        log.debug("## SearchClient List : {}", searchClients);
        log.debug("## Params : {}", params);

        if (Objects.nonNull(params.getFirstTryService())) {
            SearchClient firstTryClient = searchClients.stream()
                    .filter(client -> client.getExternalService() == params.getFirstTryService())
                    .findFirst().orElseThrow(() -> new RuntimeException("Appointed Service Client Not Exists"));
            try {
                BlogSearchResult result = firstTryClient.searchBlog(params.getKeyword(), params.getSuitableSort(firstTryClient.getExternalService()), params.getPage(), params.getPageSize());
                log.debug("## FirstTryClient Result : {}", result);
                return result;
            } catch (RuntimeException e) {
                log.warn("## Exception : {}, - {}", e.getClass().getSimpleName(), e.getMessage());
            }
        }


        for (SearchClient searchClient : searchClients) {
            if (Objects.nonNull(params.getFirstTryService()) && searchClient.getExternalService() == params.getFirstTryService())
                continue;
            try {
                BlogSearchResult result = searchClient.searchBlog(params.getKeyword(), params.getSuitableSort(searchClient.getExternalService()), params.getPage(), params.getPageSize());
                log.debug("## result : {}", result);
                return result;
            } catch (RuntimeException e) {
                log.warn("## Exception : {}, - {}", e.getClass().getSimpleName(), e.getMessage());
            }
        }


        log.error("## Not Exists Available External Search System.");
        throw new RuntimeException("Not Exists Available External Search System.");
    }


    @Override
    public void afterPropertiesSet() {
        searchClients.add(naverSearchApiClient);
        searchClients.add(kaKaoSearchApiClient);

        searchClients.sort(Comparator.comparing(SearchClient::getDefaultOrder));
    }
}
