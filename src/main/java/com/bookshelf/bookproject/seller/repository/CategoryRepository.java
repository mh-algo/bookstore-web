package com.bookshelf.bookproject.seller.repository;

import com.bookshelf.bookproject.domain.Category;
import com.bookshelf.bookproject.seller.repository.querydsl.CustomManagementService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CustomManagementService {
}
