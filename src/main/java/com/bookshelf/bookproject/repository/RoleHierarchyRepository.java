package com.bookshelf.bookproject.repository;

import com.bookshelf.bookproject.domain.RoleHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long>, CustomRoleHierarchyRepository {
}
