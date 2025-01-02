package com.bookshelf.bookproject.publicpage.controller.dto.bookdetail;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ReviewId {
    @Positive
    @NotNull
    private Long reviewId;
}
