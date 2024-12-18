package com.bookshelf.bookproject.publicpage.repository;

import com.bookshelf.bookproject.publicpage.repository.querydsl.CustomReviewRepository;
import com.bookshelf.bookproject.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
}
