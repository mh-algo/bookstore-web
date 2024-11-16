package com.bookshelf.bookproject.repository.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthorityDto {
    private final String role;
    private final String path;
}
