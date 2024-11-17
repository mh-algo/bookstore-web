package com.bookshelf.bookproject.security.repository.querydsl;

import com.bookshelf.bookproject.security.repository.dto.RoleHierarchyDto;

import java.util.List;

public interface CustomRoleHierarchyRepository {
    List<RoleHierarchyDto> findAllByParentNotNull();
}
