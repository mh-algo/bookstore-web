package com.bookshelf.bookproject.publicpage.repository.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewListDto {
    private Long id;
    private String accountId;
    private Integer rating;
    private String context;
    private LocalDateTime createdDate;
}
