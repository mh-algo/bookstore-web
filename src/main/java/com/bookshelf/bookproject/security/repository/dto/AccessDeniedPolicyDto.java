package com.bookshelf.bookproject.security.repository.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccessDeniedPolicyDto {
    private final String requestPath;
    private final String redirectPath;
}
