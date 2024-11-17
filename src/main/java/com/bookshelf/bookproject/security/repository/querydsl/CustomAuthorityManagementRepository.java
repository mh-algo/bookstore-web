package com.bookshelf.bookproject.security.repository.querydsl;

import com.bookshelf.bookproject.security.repository.dto.AuthorityDto;

import java.util.List;

public interface CustomAuthorityManagementRepository {
    List<AuthorityDto> findAuthorityAll();
}
