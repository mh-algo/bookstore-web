package com.bookshelf.bookproject.common.repository;

import com.bookshelf.bookproject.publicpage.repository.querydsl.CustomBookProductRepository;
import com.bookshelf.bookproject.domain.BookProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookProductRepository extends JpaRepository<BookProduct, Long>, CustomBookProductRepository {
}
