package com.bookshelf.bookproject.security.repository;

import com.bookshelf.bookproject.domain.RoleHierarchy;
import com.bookshelf.bookproject.security.repository.querydsl.CustomRoleHierarchyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long>, CustomRoleHierarchyRepository {
}
