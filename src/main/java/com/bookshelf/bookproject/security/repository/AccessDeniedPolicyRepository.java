package com.bookshelf.bookproject.security.repository;

import com.bookshelf.bookproject.security.domain.AccessDeniedPolicy;
import com.bookshelf.bookproject.security.repository.querydsl.CustomAccessDeniedPolicyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessDeniedPolicyRepository extends JpaRepository<AccessDeniedPolicy, Long>, CustomAccessDeniedPolicyRepository {
}
