package com.bookshelf.bookproject.common.repository.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AllCategoryDto {
    private final Long categoryId;
    private final String categoryName;
    private final Long subcategoryId;
    private final String subcategoryName;
    private final Long subSubcategoryId;
    private final String subSubcategoryName;
}
