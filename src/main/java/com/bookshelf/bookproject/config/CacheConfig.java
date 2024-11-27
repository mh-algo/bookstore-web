package com.bookshelf.bookproject.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    @Primary
    public CacheManager cacheManager() {
        return new CaffeineCacheManager();
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfigWithTTL() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000);
    }

    @Bean
    public CacheManager cacheManagerWithTTL(Caffeine<Object, Object> caffeineConfigWithTTL) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("bookSearch", "isbnSearch");
        cacheManager.setCaffeine(caffeineConfigWithTTL);
        return cacheManager;
    }
}
