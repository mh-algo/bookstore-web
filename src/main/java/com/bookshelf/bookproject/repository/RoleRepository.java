package com.bookshelf.bookproject.repository;

import com.bookshelf.bookproject.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByType(String type);
}
