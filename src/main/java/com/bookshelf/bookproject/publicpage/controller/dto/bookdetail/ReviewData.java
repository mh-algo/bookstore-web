package com.bookshelf.bookproject.publicpage.controller.dto.bookdetail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ReviewData {
    @Positive
    @NotNull
    private Integer rating;

    @NotNull
    @NotBlank
    private String context;
}
