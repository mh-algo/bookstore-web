package com.bookshelf.bookproject.publicpage.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewLike {
    private final boolean liked;
    private final int likeCount;
}
