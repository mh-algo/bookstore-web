package com.bookshelf.bookproject.publics.repository.querydsl;

import com.bookshelf.bookproject.publics.repository.dto.BookListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBookProductRepository {
    Page<BookListDto> findAllBooks(Pageable pageable);

    Page<BookListDto> findBooksByCategory(Pageable pageable, long categoryId);
}
