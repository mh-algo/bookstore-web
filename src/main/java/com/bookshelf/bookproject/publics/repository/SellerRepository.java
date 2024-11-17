package com.bookshelf.bookproject.publics.repository;

import com.bookshelf.bookproject.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
}
