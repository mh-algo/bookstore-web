package com.bookshelf.bookproject.repository;

import com.bookshelf.bookproject.domain.Path;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityManagementRepository extends JpaRepository<Path, Long>, CustomAuthorityManagementRepository {
}
