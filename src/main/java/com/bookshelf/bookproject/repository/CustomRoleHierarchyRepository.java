package com.bookshelf.bookproject.repository;

import com.bookshelf.bookproject.repository.dto.RoleHierarchyDto;

import java.util.List;

public interface CustomRoleHierarchyRepository {
    List<RoleHierarchyDto> findAllByParentNotNull();
}
