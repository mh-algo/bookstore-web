package com.bookshelf.bookproject.publicpage.repository.querydsl;

import com.bookshelf.bookproject.publicpage.repository.dto.BookDetailDto;
import com.bookshelf.bookproject.publicpage.repository.dto.BookListDto;
import com.querydsl.core.Tuple;
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
import static com.bookshelf.bookproject.domain.QCategory.category;
import static com.bookshelf.bookproject.domain.QSubSubcategory.subSubcategory;
import static com.bookshelf.bookproject.domain.QSubcategory.subcategory;

@RequiredArgsConstructor
public class CustomBookProductRepositoryImpl implements CustomBookProductRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookListDto> findAllBooks(Pageable pageable) {
        List<BookListDto> bookProducts = createBookListDto(
                queryFactory
                    .select(
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
                            book.description)
                    .from(bookProduct)
                    .join(bookProduct.book, book)
                    .orderBy(bookProduct.createdDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch()
        );

        JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(bookProduct);

        return PageableExecutionUtils.getPage(bookProducts, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<BookListDto> findBooksByCategory(Pageable pageable, long categoryId) {
        List<BookListDto> bookProducts = createBookListDto(
                queryFactory
                        .select(
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
                            book.description)
                    .from(bookProduct)
                    .join(bookProduct.book, book)
                    .where(bookProduct.subSubcategory.id.eq(categoryId))
                    .orderBy(bookProduct.createdDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch()
        );

        JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(bookProduct)
                .where(bookProduct.subSubcategory.id.eq(categoryId));

        return PageableExecutionUtils.getPage(bookProducts, pageable, countQuery::fetchOne);
    }

    private static List<BookListDto> createBookListDto(List<Tuple> bookProducts) {
        return bookProducts.stream()
                .map(tuple -> (BookListDto) BookListDto.builder()
                        .id(tuple.get(bookProduct.id))
                        .customPrice(tuple.get(bookProduct.price))
                        .discount(tuple.get(bookProduct.discount))
                        .imageUrl(tuple.get(bookProduct.mainImageUrl))
                        .title(tuple.get(book.title))
                        .subtitle(tuple.get(book.subtitle))
                        .defaultImageUrl(tuple.get(book.imageUrl))
                        .author(tuple.get(book.author))
                        .publisher(tuple.get(book.publisher))
                        .publishedDate(tuple.get(book.publishedDate))
                        .price(tuple.get(book.price))
                        .description(tuple.get(book.description))
                        .build()
                ).toList();
    }

    @Override
    public BookDetailDto findBookDetailById(long id) {
        return createBookDetailDto(
                queryFactory
                    .select(
                            bookProduct.id,
                            bookProduct.price,
                            bookProduct.discount,
                            bookProduct.stock,
                            bookProduct.deliveryFee,
                            bookProduct.mainImageUrl,
                            book.title,
                            book.subtitle,
                            book.imageUrl,
                            book.author,
                            book.publisher,
                            book.isbn,
                            book.publishedDate,
                            book.price,
                            book.description,
                            category.name,
                            subcategory.name,
                            subSubcategory.name,
                            subSubcategory.id
                            )
                    .from(bookProduct)
                    .join(bookProduct.book, book)
                    .join(bookProduct.subSubcategory, subSubcategory)
                    .join(subSubcategory.subcategory, subcategory)
                    .join(subcategory.category, category)
                    .where(bookProduct.id.eq(id))
                    .fetchOne()
        );
    }

    private static BookDetailDto createBookDetailDto(Tuple tuple) {
        if (tuple == null) {
            return BookDetailDto.empty();
        }

        return BookDetailDto.builder()
                .id(tuple.get(bookProduct.id))
                .customPrice(tuple.get(bookProduct.price))
                .discount(tuple.get(bookProduct.discount))
                .stock(tuple.get(bookProduct.stock))
                .deliveryFee(tuple.get(bookProduct.deliveryFee))
                .imageUrl(tuple.get(bookProduct.mainImageUrl))
                .title(tuple.get(book.title))
                .subtitle(tuple.get(book.subtitle))
                .defaultImageUrl(tuple.get(book.imageUrl))
                .author(tuple.get(book.author))
                .publisher(tuple.get(book.publisher))
                .isbn(tuple.get(book.isbn))
                .publishedDate(tuple.get(book.publishedDate))
                .price(tuple.get(book.price))
                .description(tuple.get(book.description))
                .category(tuple.get(category.name))
                .subcategory(tuple.get(subcategory.name))
                .subSubcategory(tuple.get(subSubcategory.name))
                .subSubcategoryId(tuple.get(subSubcategory.id))
                .build();
    }
}
