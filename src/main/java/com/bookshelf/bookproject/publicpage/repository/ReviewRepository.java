package com.bookshelf.bookproject.publicpage.repository;

import com.bookshelf.bookproject.publicpage.repository.querydsl.CustomReviewRepository;
import com.bookshelf.bookproject.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    // 엔티티로 조회할 경우 2차 캐시와 db 일관성 문제 발생 가능성 있음
    // 나중에 Review 엔티티 조회 메서드를 추가할 경우 조치 필요!!!
    @Modifying
    @Query("update Review r set r.likeCount = :likeCount, r.lastModifiedDate = CURRENT_TIMESTAMP where r.id = :id")
    void updateLikeCountById(@Param("id") Long id, @Param("likeCount") Integer likeCount);
}
