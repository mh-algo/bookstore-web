package com.bookshelf.bookproject.repository;

import com.bookshelf.bookproject.domain.RoleManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleManagementRepository extends JpaRepository<RoleManagement, Long> {
}
