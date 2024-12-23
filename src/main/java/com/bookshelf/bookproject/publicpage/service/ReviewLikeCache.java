package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.common.AccountCache;
import com.bookshelf.bookproject.config.CustomCacheResolver;
import com.bookshelf.bookproject.domain.Account;
import com.bookshelf.bookproject.domain.LikeStatus;
import com.bookshelf.bookproject.domain.Review;
import com.bookshelf.bookproject.publicpage.repository.LikeStatusRepository;
import com.bookshelf.bookproject.publicpage.repository.ReviewRepository;
import com.bookshelf.bookproject.publicpage.repository.dto.ReviewListDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.bookshelf.bookproject.config.CustomCacheResolver.REVIEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewLikeCache {
    private final LikeStatusRepository likeStatusRepository;
    private final ReviewRepository reviewRepository;
    private final CacheResolver cacheResolver;
    private final AccountCache accountCache;
    private final ReviewCache reviewCache;
    private CacheManager reviewCacheManager;

    private static final String LIKE_STATUS = "review:likeStatus";
    private static final String LIKE_COUNT = "review:likeCount";

    private final Set<String> updatedLikeStatus = ConcurrentHashMap.newKeySet();
    private final Set<Long> updatedLikeCounts = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void initialize() {
        if (cacheResolver instanceof CustomCacheResolver resolver) {
            this.reviewCacheManager = resolver.resolveCacheManager(REVIEW);
        } else {
            throw new IllegalStateException("Initialization failed: CacheResolver typecasting 실패");
        }
    }

    @Cacheable(value = "review:reviewList", key = "#bookId", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public List<ReviewListDto> getReviewList(Long bookId) {
        return reviewRepository.findReviewListByBookId(bookId);
    }

    @Cacheable(value = "review:likeStatusSet", key = "#bookId + ':' + #accountEntityId", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public Set<Long> getLikeStatusSet(Long bookId, Long accountEntityId) {
        return likeStatusRepository.findReviewId(bookId, accountEntityId);
    }



    @Cacheable(value = LIKE_STATUS, key = "#reviewId + ':' + #accountId", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public boolean isLiked(Long reviewId, Long accountEntityId, String accountId) {
        return likeStatusRepository.existsByReviewIdAndAccountId(reviewId, accountEntityId);
    }

    @Cacheable(value = LIKE_COUNT, key = "#reviewId", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public int getLikeCount(Long reviewId) {
        Review review = getReview(reviewId);
        return review.isEmpty() ? 0 : review.getLikeCount();
    }

    private Review getReview(Long reviewId) {
        return reviewCache.getReviewById(reviewId);
    }

    @CachePut(value = LIKE_STATUS, key = "#reviewId + ':' + #accountId", cacheResolver = "cacheResolver")
    public boolean updateLikeStatus(Long reviewId, String accountId, boolean liked) {
        return !liked;
    }

    @CachePut(value = LIKE_COUNT, key = "#reviewId", cacheResolver = "cacheResolver")
    public int updateLikeCount(Long reviewId, boolean liked, int likeCount) {
        updatedLikeCounts.add(reviewId);
        return liked ? likeCount + 1 : likeCount - 1;
    }

    public int getValidLikeCount(Long reviewId, int likeCount) {
        Cache cache = reviewCacheManager.getCache(LIKE_COUNT);
        if (cache == null) {
            return likeCount;
        }
        Integer likeCntFromCache = cache.get(reviewId, Integer.class);
        return Objects.requireNonNullElse(likeCntFromCache, likeCount);
    }

    public boolean getValidLikeStatus(Long reviewId, String accountId, boolean liked) {
        Cache cache = reviewCacheManager.getCache(LIKE_STATUS);
        if (cache == null) {
            return liked;
        }
        Boolean likeStatusFromCache = cache.get(generateCacheKey(reviewId, accountId), Boolean.class);
        return Objects.requireNonNullElse(likeStatusFromCache, liked);
    }

    private static String generateCacheKey(Long primaryKey, String secondaryKey) {
        return primaryKey + ":" + secondaryKey;
    }

    public void saveCacheKeys(Long reviewId, String accountId) {
        String cacheKey = generateCacheKey(reviewId, accountId);
        if (updatedLikeStatus.contains(cacheKey)) {
            updatedLikeStatus.remove(cacheKey);
        } else {
            updatedLikeStatus.add(cacheKey);
        }
        updatedLikeCounts.add(reviewId);
    }

    private Account getAccount(String accountId) {
        return accountCache.getAccount(accountId);
    }

    @Scheduled(cron = "${schedule.like.cron}")
    @Transactional
    public void syncLikeStatusToDatabase() {
        Cache cache = reviewCacheManager.getCache(LIKE_STATUS);
        if (cache != null) {
            updatedLikeStatus.forEach(cacheKey -> {
                String[] keys = cacheKey.split(":");
                Long reviewId = Long.parseLong(keys[0]);
                String accountId = keys[1];
                Account account = getAccount(accountId);

                Boolean liked = cache.get(cacheKey, Boolean.class);
                if (Boolean.TRUE.equals(liked)) {
                    Review review = getReview(reviewId);
                    LikeStatus likeStatus = new LikeStatus(review, account);
                    likeStatusRepository.save(likeStatus);
                } else if (Boolean.FALSE.equals(liked)){
                    likeStatusRepository.deleteByReviewIdAndAccountId(reviewId, account.getId());
                } else {
                    log.error("Failed to delete likeStatus: liked is null. Cache Key: {}", cacheKey);
                }
            });
            updatedLikeStatus.clear();
        }
    }

    @Scheduled(cron = "${schedule.like.cron}")
    @Transactional
    public void syncLikeCountsToDatabase() {
        Cache cache = reviewCacheManager.getCache(LIKE_COUNT);
        if (cache != null) {
            updatedLikeCounts.forEach(reviewId -> {
                Integer likeCount = cache.get(reviewId, Integer.class);
                if (likeCount != null) {
                    reviewRepository.updateLikeCountById(reviewId, likeCount);
                } else {
                    log.error("Failed to update likeCount: likeCount is null. Cache Key: {}", reviewId);
                }
            });
            updatedLikeCounts.clear();
        }
    }
}
