package com.bookshelf.bookproject.seller.repository.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AllCategoryDto {
    private final String categoryName;
    private final String  subCategoryName;
    private final String  subSubCategoryName;
}
