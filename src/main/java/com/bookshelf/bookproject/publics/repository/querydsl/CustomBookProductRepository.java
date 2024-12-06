package com.bookshelf.bookproject.publics.repository.querydsl;

import com.bookshelf.bookproject.publics.repository.dto.BookListDto;

import java.util.List;

public interface CustomBookProductRepository {
    List<BookListDto> findAllBookProducts();
}
