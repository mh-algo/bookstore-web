package com.bookshelf.bookproject.repository;

import com.bookshelf.bookproject.repository.dto.AuthorityDto;

import java.util.List;

public interface CustomAuthorityManagementRepository {
    List<AuthorityDto> findAuthorityAll();
}
