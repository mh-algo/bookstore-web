package com.bookshelf.bookproject.seller.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class SubcategoryDto {
    private final String name;
    private final Map<Long, SubSubcategoryDto> subSubcategories;
}
