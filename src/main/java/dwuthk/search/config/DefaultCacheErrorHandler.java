package dwuthk.search.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Slf4j
public class DefaultCacheErrorHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.warn("## CacheError GET : CacheName : {}, Key :{}, Exception : {}, Message : {}", cache.getName(), key, exception.getClass().getSimpleName(), exception.getMessage());
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.warn("## CacheError PUT : CacheName : {}, Key :{}, Exception : {}, Message : {}", cache.getName(), key, exception.getClass().getSimpleName(), exception.getMessage());
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.warn("## CacheError EVICT : CacheName : {}, Key :{}, Exception : {}, Message : {}", cache.getName(), key, exception.getClass().getSimpleName(), exception.getMessage());

    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.warn("## CacheError CLEAR : CacheName : {}, Exception : {}, Message : {}", cache.getName(), exception.getClass().getSimpleName(), exception.getMessage());

    }
}
