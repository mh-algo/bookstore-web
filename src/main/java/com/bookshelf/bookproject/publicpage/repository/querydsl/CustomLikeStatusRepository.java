package com.bookshelf.bookproject.publicpage.repository.querydsl;

import java.util.Set;

public interface CustomLikeStatusRepository {
    Set<Long> findReviewIdByBookProductId(Long bookProductId);
    Boolean existsByReviewIdAndAccountId(Long reviewId, String accountId);
}
