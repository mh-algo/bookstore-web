package com.bookshelf.bookproject.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.bookshelf.bookproject.config.CustomCacheResolver.*;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    @Primary
    public CacheManager cacheManager() {
        return new CaffeineCacheManager();
    }

    @Bean
    public Caffeine<Object, Object> accountConfig() {
        return Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .maximumSize(500);
    }

    @Bean
    public Caffeine<Object, Object> sellerConfig() {
        return Caffeine.newBuilder()
                .expireAfterAccess(6, TimeUnit.HOURS)
                .maximumSize(1000);
    }

    @Bean
    public Caffeine<Object, Object> bookListConfig() {
        return Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .maximumSize(1000);
    }

    @Bean
    public CacheManager accountCacheManager(Caffeine<Object, Object> accountConfig) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(accountConfig);
        return cacheManager;
    }

    @Bean
    public CacheManager sellerCacheManager(Caffeine<Object, Object> sellerConfig) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(sellerConfig);
        return cacheManager;
    }

    @Bean
    public CacheManager bookListCacheManager(Caffeine<Object, Object> bookListConfig) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(bookListConfig);
        return cacheManager;
    }

    @Bean
    public CacheResolver cacheResolver(CacheManager cacheManager,
                                       @Qualifier("accountCacheManager") CacheManager accountCacheManager,
                                       @Qualifier("sellerCacheManager") CacheManager sellerCacheManager,
                                       @Qualifier("bookListCacheManager") CacheManager bookListCacheManager) {
        return new CustomCacheResolver(
                Map.of(
                        ACCOUNT, accountCacheManager,
                        SELLER, sellerCacheManager,
                        BOOK_LIST, bookListCacheManager
                ), cacheManager
        );
    }
}
