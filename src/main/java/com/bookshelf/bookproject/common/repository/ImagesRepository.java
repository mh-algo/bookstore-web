package com.bookshelf.bookproject.common.repository;

import com.bookshelf.bookproject.domain.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    @Query("select i.subImageUrl from Images i where i.bookProduct.id = :bookProductId")
    List<String> findSubImageUrlByBookProductId(@Param("bookProductId") Long bookProductId);
}
