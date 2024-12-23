package com.bookshelf.bookproject.publicpage.repository.querydsl;

import java.util.Set;

public interface CustomLikeStatusRepository {
    Set<Long> findReviewId(Long bookProductId, Long accountEntityId);
    Boolean existsByReviewIdAndAccountId(Long reviewId, Long accountEntityId);
}
