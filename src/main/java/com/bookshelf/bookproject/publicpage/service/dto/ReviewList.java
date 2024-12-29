package com.bookshelf.bookproject.publicpage.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewList {
    private final Long id;
    private final String accountId;
    private final Integer rating;
    private final String context;
    private final Integer likeCount;
    private final boolean likeStatus;
    private final LocalDateTime createdDate;
    private final boolean reviewOwner;
}
