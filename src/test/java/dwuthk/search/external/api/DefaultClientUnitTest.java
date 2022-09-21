package dwuthk.search.external.api;

import dwuthk.search.common.exception.CustomException;
import dwuthk.search.external.api.search.kakao.KaKaoSearchApiClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


@Slf4j
@DisplayName("유닛 - 기본 클라이언트 인터페이스 로직")
@ExtendWith({MockitoExtension.class})
class DefaultClientUnitTest {

    @Spy
    private KaKaoSearchApiClient kaKaoSearchApiClient;

    @Test
    public void testRetryFallbackThrowable() {
        RuntimeException exception = new RuntimeException();

        Throwable retryFallbackThrowable = kaKaoSearchApiClient.defaultRetryFallbackException(exception);

        log.info("## Throwable Message : {}", retryFallbackThrowable.getMessage());

        Assertions.assertTrue(retryFallbackThrowable instanceof CustomException);
    }

    @Test
    public void testCircuitBreakFallbackThrowable() {
        RuntimeException exception = new RuntimeException();

        Throwable retryFallbackThrowable = kaKaoSearchApiClient.defaultCircuitBreakFallbackException(exception);

        log.info("## Throwable Message : {}", retryFallbackThrowable.getMessage());

        Assertions.assertEquals(exception, retryFallbackThrowable);
    }

}