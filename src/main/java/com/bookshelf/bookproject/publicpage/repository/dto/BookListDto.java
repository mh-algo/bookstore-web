package com.bookshelf.bookproject.publicpage.repository.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class BookListDto {
    // BookProduct
    private final Long id;
    private final Integer customPrice;
    private final Integer discount;
    private final String imageUrl;

    // Book
    private final String title;
    private final String subtitle;
    private final String defaultImageUrl;
    private final String author;
    private final String publisher;
    private final LocalDate publishedDate;
    private final Integer price;
    private final String description;
}
