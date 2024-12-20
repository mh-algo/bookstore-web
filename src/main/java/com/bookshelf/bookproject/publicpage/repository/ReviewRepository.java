package com.bookshelf.bookproject.publicpage.repository;

import com.bookshelf.bookproject.publicpage.repository.querydsl.CustomReviewRepository;
import com.bookshelf.bookproject.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    @Query("select r.likeCount from Review r where r.id = :id")
    Optional<Integer> findLikeCountById(@Param("id") Long id);
}
