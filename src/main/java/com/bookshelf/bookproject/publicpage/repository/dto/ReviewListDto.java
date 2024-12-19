package com.bookshelf.bookproject.publicpage.repository.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewListDto {
    private final Long id;
    private final String accountId;
    private final Integer rating;
    private final String context;
    private final Integer likeCount;
    private final LocalDateTime createdDate;
}
