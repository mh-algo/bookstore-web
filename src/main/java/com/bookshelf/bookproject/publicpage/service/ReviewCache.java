package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.domain.Review;
import com.bookshelf.bookproject.publicpage.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookshelf.bookproject.config.CacheConstants.CACHE_RESOLVER;
import static com.bookshelf.bookproject.config.CacheConstants.REVIEW;

@Service
@RequiredArgsConstructor
public class ReviewCache {
    private final ReviewRepository reviewRepository;

    @Cacheable(value = REVIEW, key = "#id", unless = "#result.isEmpty()", cacheResolver = CACHE_RESOLVER)
    @Transactional(readOnly = true)
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(Review.empty());
    }
}
