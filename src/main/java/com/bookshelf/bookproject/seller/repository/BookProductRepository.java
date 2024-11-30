package com.bookshelf.bookproject.seller.repository;

import com.bookshelf.bookproject.domain.BookProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookProductRepository extends JpaRepository<BookProduct, Long> {
}
