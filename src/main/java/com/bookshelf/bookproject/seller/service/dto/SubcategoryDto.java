package com.bookshelf.bookproject.seller.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SubcategoryDto {
    private final String name;
    private final List<SubSubcategoryDto> subSubcategories;
}
