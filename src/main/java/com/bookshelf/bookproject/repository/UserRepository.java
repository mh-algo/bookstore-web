package com.bookshelf.bookproject.repository;

import com.bookshelf.bookproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
