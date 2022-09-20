package dwuthk.search.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig extends JCacheConfigurerSupport {


    @Override
    public CacheErrorHandler errorHandler() {
        return new DefaultCacheErrorHandler();
    }

}
