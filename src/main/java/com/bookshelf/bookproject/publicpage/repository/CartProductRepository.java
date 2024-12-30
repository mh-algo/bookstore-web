package com.bookshelf.bookproject.publicpage.repository;

import com.bookshelf.bookproject.domain.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
}
