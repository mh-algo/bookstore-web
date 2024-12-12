package com.bookshelf.bookproject.publicpage.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BookListInfo {
    private final Long id;
    private final String title;
    private final String subtitle;
    private final String imageUrl;
    private final String author;
    private final String publisher;
    private final LocalDate publishedDate;
    private final Integer price;
    private final String discountRate;
    private final String description;
}
