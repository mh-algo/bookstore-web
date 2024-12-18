package com.bookshelf.bookproject.publicpage.repository.querydsl;

import com.bookshelf.bookproject.publicpage.repository.dto.ReviewListDto;

import java.util.List;

public interface CustomReviewRepository {
    List<ReviewListDto> findReviewListByBookId(Long bookId);
}
