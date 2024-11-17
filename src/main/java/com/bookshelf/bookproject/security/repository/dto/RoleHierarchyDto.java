package com.bookshelf.bookproject.security.repository.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleHierarchyDto {
    private final String role;
    private final String parent;
}
