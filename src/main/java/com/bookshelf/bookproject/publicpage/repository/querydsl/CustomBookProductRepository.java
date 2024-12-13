package com.bookshelf.bookproject.publicpage.repository.querydsl;

import com.bookshelf.bookproject.publicpage.repository.dto.BookDetailDto;
import com.bookshelf.bookproject.publicpage.repository.dto.BookListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBookProductRepository {
    Page<BookListDto> findAllBooks(Pageable pageable);

    Page<BookListDto> findBooksByCategory(Pageable pageable, long categoryId);

    BookDetailDto findBookDetailById(long id);
}
