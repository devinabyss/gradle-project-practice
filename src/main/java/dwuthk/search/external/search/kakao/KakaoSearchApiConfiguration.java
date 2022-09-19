package dwuthk.search.external.search.kakao;


import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;


@Slf4j
public class KakaoSearchApiConfiguration {

    @Bean
    public RequestInterceptor kakaoSearchRequestInterceptor(
            KakaoSearchApiClientProperties properties
    ) {
        return template -> {
            template
                    .header("Authorization", "KakaoAK " + properties.apiKey());


        };
    }

    @ConfigurationProperties(prefix = "external.kakao-search")
    private record KakaoSearchApiClientProperties(String url, String apiKey) {

    }
}
