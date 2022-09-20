package dwuthk.search.external.api.search.naver;


import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;


@Slf4j
public class NaverSearchApiConfiguration {


    @Bean
    public RequestInterceptor naverSearchRequestInterceptor(
            NaverSearchApiClientProperties properties
    ) {
        return template -> {
            template
                    .header("X-Naver-Client-Id", properties.clientId())
                    .header("X-Naver-Client-Secret", properties.clientSecret());


        };
    }


    @ConfigurationProperties(prefix = "external.naver-search")
    private record NaverSearchApiClientProperties(String url, String clientId, String clientSecret) {
    }
}
