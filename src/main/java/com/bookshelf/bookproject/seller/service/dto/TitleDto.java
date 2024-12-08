package com.bookshelf.bookproject.seller.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TitleDto {
    private final String title;
    private final String subtitle;
}
