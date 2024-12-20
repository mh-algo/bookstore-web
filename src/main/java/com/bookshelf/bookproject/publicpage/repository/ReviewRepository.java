package com.bookshelf.bookproject.publicpage.repository;

import com.bookshelf.bookproject.publicpage.repository.querydsl.CustomReviewRepository;
import com.bookshelf.bookproject.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    Optional<Integer> findLikeCountById(Long id);
}
