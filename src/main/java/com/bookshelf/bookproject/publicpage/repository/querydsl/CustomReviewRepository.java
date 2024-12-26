package com.bookshelf.bookproject.publicpage.repository.querydsl;

import com.bookshelf.bookproject.publicpage.repository.dto.ReviewListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomReviewRepository {
    Page<ReviewListDto> findReviewListByBookId(Pageable pageable, Long bookId);
}
