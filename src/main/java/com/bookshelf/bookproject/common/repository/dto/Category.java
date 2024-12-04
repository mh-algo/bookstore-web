package com.bookshelf.bookproject.common.repository.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public abstract class Category {
    @NotNull
    @Positive
    protected Long categoryId;

    @NotNull
    @Positive
    protected Long subcategoryId;

    @NotNull
    @Positive
    protected Long subSubcategoryId;
}
