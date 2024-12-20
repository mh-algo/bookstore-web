package com.bookshelf.bookproject.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Parameter;
import java.util.*;

public class CustomCacheResolver implements CacheResolver {
    public static final String ACCOUNT = "account";
    public static final String SELLER = "seller";
    public static final String BOOK_LIST = "bookList";
    public static final String REVIEW = "review";

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

    public CacheManager resolveCacheManager(String cacheName) {
        // 캐시 이름에 따른 캐시 매니저 매핑
        if (cacheName.startsWith(ACCOUNT)) {
            return this.cacheManagers.get(ACCOUNT);
        }

        if (cacheName.startsWith(SELLER)) {
            return this.cacheManagers.get(SELLER);
        }

        if (cacheName.startsWith(BOOK_LIST)) {
            return this.cacheManagers.get(BOOK_LIST);
        }

        return defaultCacheManager;
    }

    private Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        Object[] args = context.getArgs();
        String[] parameterNames = Arrays.stream(context.getMethod().getParameters())
                .map(Parameter::getName) // 파라미터 이름 가져오기
                .toArray(String[]::new);

        return context.getOperation().getCacheNames().stream()
                .map(it -> new SpelExpressionParser().parseExpression(it, ParserContext.TEMPLATE_EXPRESSION)
                        .getValue(getStandardEvaluationContext(args, parameterNames), String.class))
                .toList();
    }

    private static StandardEvaluationContext getStandardEvaluationContext(Object[] args, String[] parameterNames) {
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        for (int i = 0; i < args.length; i++) {
            evaluationContext.setVariable(parameterNames[i], args[i]);
        }
        return evaluationContext;
    }
}