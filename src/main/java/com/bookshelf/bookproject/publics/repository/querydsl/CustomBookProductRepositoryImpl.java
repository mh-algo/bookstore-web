package com.bookshelf.bookproject.publics.repository.querydsl;

import com.bookshelf.bookproject.publics.repository.dto.BookListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookshelf.bookproject.domain.QBook.book;
import static com.bookshelf.bookproject.domain.QBookProduct.bookProduct;

@RequiredArgsConstructor
public class CustomBookProductRepositoryImpl implements CustomBookProductRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookListDto> findAllBookProducts() {
        return queryFactory
                .select(Projections.constructor(BookListDto.class,
                        bookProduct.id,
                        bookProduct.price,
                        bookProduct.discount,
                        bookProduct.mainImageUrl,
                        book.title,
                        book.imageUrl,
                        book.author,
                        book.publisher,
                        book.publishedDate,
                        book.price,
                        book.description))
                .from(bookProduct)
                .join(bookProduct.book, book)
                .fetch();
    }
}
