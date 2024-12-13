package com.bookshelf.bookproject.publicpage.service.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class BookList {
    private final Long id;
    private final String title;
    private final String subtitle;
    private final String imageUrl;
    private final String author;
    private final String publisher;
    private final LocalDate publishedDate;
    private final Integer discountPrice;
    private final String discountRate;
    private final String description;

    public boolean isEmpty() {
        return id == null || id == -1L;
    }
}
