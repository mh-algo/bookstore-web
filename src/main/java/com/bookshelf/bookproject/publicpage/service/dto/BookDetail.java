package com.bookshelf.bookproject.publicpage.service.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BookDetail extends BookList {
    private final Integer price;
    private final Integer stock;
    private final Integer deliveryFee;
    private final String isbn;
    private final String category;
    private final String subcategory;
    private final String subSubcategory;
    private final Long subSubcategoryId;
}
