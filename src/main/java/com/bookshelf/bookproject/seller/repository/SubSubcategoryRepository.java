package com.bookshelf.bookproject.seller.repository;

import com.bookshelf.bookproject.domain.SubSubcategory;
import com.bookshelf.bookproject.seller.repository.querydsl.CustomSubSubcategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubSubcategoryRepository extends JpaRepository<SubSubcategory, Long>, CustomSubSubcategoryRepository {
}
