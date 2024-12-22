package com.bookshelf.bookproject.seller.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SearchInfo {
    private String lastBuildDate;
    private String total;
    private String start;
    private Integer display;
    private List<BookInfo> items;

    public SearchInfo(String lastBuildDate, String total, String start, Integer display, List<BookInfo> items) {
        this.lastBuildDate = lastBuildDate;
        this.total = total;
        this.start = start;
        this.display = display;
        this.items = items;
    }

    public static SearchInfo empty() {
        return new SearchInfo("", "", "", 0, new ArrayList<>());
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
