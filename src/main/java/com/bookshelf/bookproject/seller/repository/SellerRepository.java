package com.bookshelf.bookproject.seller.repository;

import com.bookshelf.bookproject.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByAccountId(String accountId);
}
