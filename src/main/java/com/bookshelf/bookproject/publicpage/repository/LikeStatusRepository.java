package com.bookshelf.bookproject.publicpage.repository;

import com.bookshelf.bookproject.domain.LikeStatus;
import com.bookshelf.bookproject.publicpage.repository.querydsl.CustomLikeStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeStatusRepository extends JpaRepository<LikeStatus, Long>, CustomLikeStatusRepository {
    // 엔티티로 조회할 경우 2차 캐시와 db 일관성 문제 발생 가능성 있음
    // 나중에 LikeStatus 엔티티 조회 메서드를 추가할 경우 조치 필요!!!
    @Modifying
    @Query("delete from LikeStatus l where l.review.id = :reviewId and l.account.id = :accountEntityId")
    void deleteByReviewIdAndAccountId(@Param("reviewId") Long reviewId, @Param("accountEntityId") Long accountEntityId);
}
