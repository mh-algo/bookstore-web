package com.bookshelf.bookproject.seller.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchInfo {
    private String lastBuildDate;
    private String total;
    private String start;
    private Integer display;
    private List<BookInfo> items;
}
