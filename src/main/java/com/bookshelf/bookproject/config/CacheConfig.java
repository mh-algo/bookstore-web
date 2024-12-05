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
    public Caffeine<Object, Object> caffeineConfigWith10Min() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000);
    }

    @Bean
    public CacheManager cacheManagerWith10Min(Caffeine<Object, Object> caffeineConfigWith10Min) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("bookSearch", "isbnSearch");
        cacheManager.setCaffeine(caffeineConfigWith10Min);
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfigWith24Hours() {
        return Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .maximumSize(1000);
    }

    @Bean
    public CacheManager cacheManagerWith24Hours(Caffeine<Object, Object> caffeineConfigWith24Hours) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("bookData", "roleInfo");
        cacheManager.setCaffeine(caffeineConfigWith24Hours);
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfigWith1Hour() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(500);
    }

    @Bean
    public CacheManager cacheManagerWith1Hour(Caffeine<Object, Object> caffeineConfigWith1Hour) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("accountInfo");
        cacheManager.setCaffeine(caffeineConfigWith1Hour);
        return cacheManager;
    }
}
