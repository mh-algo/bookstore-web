package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.domain.Review;
import com.bookshelf.bookproject.publicpage.repository.ReviewRepository;
import com.bookshelf.bookproject.publicpage.repository.dto.ReviewListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookshelf.bookproject.config.CacheConstants.CACHE_RESOLVER;
import static com.bookshelf.bookproject.config.CacheConstants.REVIEW;

@Service
@RequiredArgsConstructor
public class ReviewCache {
    private final ReviewRepository reviewRepository;

    @Cacheable(value = REVIEW + ":#{#bookId}", key = "#pageable.pageNumber", cacheResolver = CACHE_RESOLVER)
    @Transactional(readOnly = true)
    public Page<ReviewListDto> getReviewList(Pageable pageable, Long bookId) {
        return reviewRepository.findReviewListByBookId(pageable, bookId);
    }

    @Cacheable(value = REVIEW, key = "#id", unless = "#result.isEmpty()", cacheResolver = CACHE_RESOLVER)
    @Transactional(readOnly = true)
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(Review.empty());
    }
}
