package com.bookshelf.bookproject.publicpage.repository;

import com.bookshelf.bookproject.domain.LikeStatus;
import com.bookshelf.bookproject.publicpage.repository.querydsl.CustomLikeStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeStatusRepository extends JpaRepository<LikeStatus, Long>, CustomLikeStatusRepository {
    Boolean existsByReviewIdAndAccountId(Long reviewId, Long accountId);
}
