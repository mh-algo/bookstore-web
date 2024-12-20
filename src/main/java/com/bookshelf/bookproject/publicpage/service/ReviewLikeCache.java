package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.config.CustomCacheResolver;
import com.bookshelf.bookproject.publicpage.repository.LikeStatusRepository;
import com.bookshelf.bookproject.publicpage.repository.ReviewRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.bookshelf.bookproject.config.CustomCacheResolver.REVIEW;

@Service
@RequiredArgsConstructor
public class ReviewLikeCache {
    private final LikeStatusRepository likeStatusRepository;
    private final ReviewRepository reviewRepository;
    private final CacheResolver cacheResolver;
    private CacheManager reviewCacheManager;

    @PostConstruct
    public void initialize() {
        if (cacheResolver instanceof CustomCacheResolver resolver) {
            this.reviewCacheManager = resolver.resolveCacheManager(REVIEW);
        } else {
            throw new IllegalStateException("Initialization failed: CacheResolver typecasting 실패");
        }
    }

    @Cacheable(value = "review:likeStatus", key = "#reviewId + ':' + #accountId", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public boolean isLiked(Long reviewId, String accountId) {
        return likeStatusRepository.existsByReviewIdAndAccountId(reviewId, accountId);
    }

    @Cacheable(value = "review:likeCnt", key = "#reviewId", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public int getLikeCount(Long reviewId) {
        return reviewRepository.findLikeCountById(reviewId).orElse(0);
    }

    @CachePut(value = "review:likeStatus", key = "#reviewId + ':' + #accountId", cacheResolver = "cacheResolver")
    public boolean updateLikeStatus(Long reviewId, String accountId, boolean liked) {
        return !liked;
    }

    @CachePut(value = "review:likeCnt", key = "#reviewId", cacheResolver = "cacheResolver")
    public int updateLikeCount(Long reviewId, boolean liked, int likeCount) {
        return liked ? likeCount + 1 : likeCount - 1;
    }

    public int getValidLikeCount(Long reviewId, int likeCount) {
        Cache cache = reviewCacheManager.getCache("review:likeCnt");
        if (cache == null) {
            return likeCount;
        }
        Integer likeCntFromCache = cache.get(reviewId, Integer.class);
        return Objects.requireNonNullElse(likeCntFromCache, likeCount);
    }

    public boolean getValidLikeStatus(Long reviewId, String accountId, boolean liked) {
        Cache cache = reviewCacheManager.getCache("review:likeStatus");
        if (cache == null) {
            return liked;
        }
        Boolean likeStatusFromCache = cache.get(generateCacheKey(reviewId, accountId), Boolean.class);
        return Objects.requireNonNullElse(likeStatusFromCache, liked);
    }

    private static String generateCacheKey(Long primaryKey, String secondaryKey) {
        return primaryKey + ":" + secondaryKey;
    }
}
