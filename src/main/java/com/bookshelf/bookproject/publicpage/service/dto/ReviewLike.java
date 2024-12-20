package com.bookshelf.bookproject.publicpage.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewLike {
    private final boolean liked;
    private final int likeCount;

    public static ReviewLike empty() {
        return new ReviewLike(false, -1);
    }

    public boolean isEmpty() {
        return likeCount < 0;
    }
}
