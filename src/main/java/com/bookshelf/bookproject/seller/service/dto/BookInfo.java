package com.bookshelf.bookproject.seller.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookInfo {
    private String title;
    private String link;
    private String image;
    private String author;
    private Integer discount;
    private String publisher;
    private String isbn;
    private String description;
    private String pubdate;

    @Builder
    public BookInfo(String title, String link, String image, String author, Integer discount, String publisher, String isbn, String description, String pubdate) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.author = author;
        this.discount = discount;
        this.publisher = publisher;
        this.isbn = isbn;
        this.description = description;
        this.pubdate = pubdate;
    }
}
