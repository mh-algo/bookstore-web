package com.bookshelf.bookproject.publicpage.repository.querydsl;

import com.bookshelf.bookproject.publicpage.repository.dto.BookListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.bookshelf.bookproject.domain.QBook.book;
import static com.bookshelf.bookproject.domain.QBookProduct.bookProduct;

@RequiredArgsConstructor
public class CustomBookProductRepositoryImpl implements CustomBookProductRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookListDto> findAllBooks(Pageable pageable) {
        List<BookListDto> bookProducts = queryFactory
                .select(Projections.constructor(BookListDto.class,
                        bookProduct.id,
                        bookProduct.price,
                        bookProduct.discount,
                        bookProduct.mainImageUrl,
                        book.title,
                        book.subtitle,
                        book.imageUrl,
                        book.author,
                        book.publisher,
                        book.publishedDate,
                        book.price,
                        book.description))
                .from(bookProduct)
                .join(bookProduct.book, book)
                .orderBy(bookProduct.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(bookProduct);

        return PageableExecutionUtils.getPage(bookProducts, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<BookListDto> findBooksByCategory(Pageable pageable, long categoryId) {
        List<BookListDto> bookProducts = queryFactory
                .select(Projections.constructor(BookListDto.class,
                        bookProduct.id,
                        bookProduct.price,
                        bookProduct.discount,
                        bookProduct.mainImageUrl,
                        book.title,
                        book.subtitle,
                        book.imageUrl,
                        book.author,
                        book.publisher,
                        book.publishedDate,
                        book.price,
                        book.description))
                .from(bookProduct)
                .join(bookProduct.book, book)
                .where(bookProduct.subSubcategory.id.eq(categoryId))
                .orderBy(bookProduct.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(bookProduct)
                .where(bookProduct.subSubcategory.id.eq(categoryId));

        return PageableExecutionUtils.getPage(bookProducts, pageable, countQuery::fetchOne);
    }
}