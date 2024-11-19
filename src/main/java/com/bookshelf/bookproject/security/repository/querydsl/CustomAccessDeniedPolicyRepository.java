package com.bookshelf.bookproject.security.repository.querydsl;

import com.bookshelf.bookproject.security.repository.dto.AccessDeniedPolicyDto;

import java.util.List;

public interface CustomAccessDeniedPolicyRepository {
    List<AccessDeniedPolicyDto> findAllAccessDeniedPolicy();
}
