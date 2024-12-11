package com.bookshelf.bookproject.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

import java.util.*;

public class CustomCacheResolver implements CacheResolver {
    protected static final String ACCOUNT = "account";
    protected static final String SELLER = "seller";

    private final Map<String, CacheManager> cacheManagers;
    private final CacheManager defaultCacheManager;

    public CustomCacheResolver(Map<String, CacheManager> cacheManagers, CacheManager defaultCacheManager) {
        this.cacheManagers = cacheManagers;
        this.defaultCacheManager = defaultCacheManager;
    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Collection<String> cacheNames = this.getCacheNames(context);
        if (cacheNames == null) {
            return Collections.emptyList();
        }
        Collection<Cache> result = new ArrayList<>(cacheNames.size());

        for (String cacheName : cacheNames) {
            CacheManager cacheManager = resolveCacheManager(cacheName);
            Cache cache = cacheManager.getCache(cacheName);

            if (cache == null) {
                throw new IllegalArgumentException("Cannot find cache named '" + cacheName + "' for " + context.getOperation());
            }

            result.add(cache);
        }
        return result;
    }

    private CacheManager resolveCacheManager(String cacheName) {
        // 캐시 이름에 따른 캐시 매니저 매핑
        if (cacheName.startsWith(ACCOUNT)) {
            return this.cacheManagers.get(ACCOUNT);
        }

        if (cacheName.startsWith(SELLER)) {
            return this.cacheManagers.get(SELLER);
        }

        return defaultCacheManager;
    }

    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        return context.getOperation().getCacheNames();
    }
}