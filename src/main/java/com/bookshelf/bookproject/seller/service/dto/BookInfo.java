package com.bookshelf.bookproject.seller.service.dto;

import lombok.Data;

@Data
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
}
