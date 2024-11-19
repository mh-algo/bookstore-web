package com.bookshelf.bookproject.security.repository;

import com.bookshelf.bookproject.security.domain.Path;
import com.bookshelf.bookproject.security.repository.querydsl.CustomAuthorityManagementRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityManagementRepository extends JpaRepository<Path, Long>, CustomAuthorityManagementRepository {
}
