package com.bookshelf.bookproject.publicpage.repository.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class BookDetailDto extends BookListDto {
    private final Integer stock;
    private final Integer deliveryFee;
    private final String isbn;
    private final String category;
    private final String subcategory;
    private final String subSubcategory;
    private final Long subSubcategoryId;

    public static BookDetailDto empty() {
        return BookDetailDto.builder()
                .id(-1L)
                .customPrice(0)
                .discount(0)
                .stock(0)
                .deliveryFee(0)
                .imageUrl("")
                .title("")
                .subtitle("")
                .defaultImageUrl("")
                .author("")
                .publisher("")
                .isbn("")
                .publishedDate(LocalDate.EPOCH)
                .price(0)
                .description("")
                .category("")
                .subcategory("")
                .subSubcategory("")
                .subSubcategoryId(0L)
                .build();
    }
}
