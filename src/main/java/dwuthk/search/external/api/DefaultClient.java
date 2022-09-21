package dwuthk.search.external.api;

import dwuthk.search.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public interface DefaultClient {
    default Throwable defaultRetryFallbackException(Throwable e) {
        return new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "External API Call Failed", e);
    }

    default Throwable defaultCircuitBreakFallbackException(Throwable e) {
        return e;
    }

}
