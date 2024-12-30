package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.common.CustomPage;
import com.bookshelf.bookproject.common.repository.BookProductRepository;
import com.bookshelf.bookproject.publicpage.repository.dto.BookListDto;
import com.bookshelf.bookproject.publicpage.service.dto.BookList;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bookshelf.bookproject.config.CacheConstants.BOOK_LIST;
import static com.bookshelf.bookproject.config.CacheConstants.CACHE_RESOLVER;
import static com.bookshelf.bookproject.publicpage.BookServiceUtils.*;

@Service
@RequiredArgsConstructor
public class BookListService {
    private static final int PAGE_SIZE = 10;
    private final BookProductRepository bookProductRepository;

    @Cacheable(value = BOOK_LIST + ":category", key = "#pageable.pageNumber", cacheResolver = CACHE_RESOLVER)
    public Page<BookList> getBookListPage(Pageable pageable) {
        Page<BookListDto> page = getBooks(pageable);
        List<BookList> bookList = page.getContent().stream()
                .map(BookListService::createBookList).toList();

        return new CustomPage<>(new PageImpl<>(bookList, page.getPageable(), page.getTotalElements()));
    }

    private Page<BookListDto> getBooks(Pageable pageable) {
        return bookProductRepository.findAllBooks(createRequestPageable(pageable));
    }

    @Cacheable(value = BOOK_LIST + ":#{#categoryId}", key = "#pageable.pageNumber", cacheResolver = CACHE_RESOLVER)
    public Page<BookList> getBookListPageByCategory(Pageable pageable, Long categoryId) {
        Page<BookListDto> page = getBooksByCategory(pageable, categoryId);
        List<BookList> bookList = page.getContent().stream()
                .map(BookListService::createBookList).toList();

        return new CustomPage<>(new PageImpl<>(bookList, page.getPageable(), page.getTotalElements()));
    }

    private Page<BookListDto> getBooksByCategory(Pageable pageable, Long categoryId) {
        return bookProductRepository.findBooksByCategory(createRequestPageable(pageable), categoryId);
    }

    private static Pageable createRequestPageable(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());
    }

    private static BookList createBookList(BookListDto bookListDto) {
        String imageUrl = getImageUrl(bookListDto.getImageUrl(), bookListDto.getDefaultImageUrl());
        Integer discount = bookListDto.getDiscount();
        Integer price = getPrice(bookListDto.getPrice(), bookListDto.getCustomPrice());
        int discountPrice = calculateDiscountPrice(price, discount);
        String discountRate = calculateDiscountRate(price, discount);

        return BookList.builder()
                .id(bookListDto.getId())
                .title(bookListDto.getTitle())
                .subtitle(bookListDto.getSubtitle())
                .imageUrl(imageUrl)
                .author(bookListDto.getAuthor())
                .publisher(bookListDto.getPublisher())
                .publishedDate(bookListDto.getPublishedDate())
                .discountPrice(discountPrice)
                .discountRate(discountRate)
                .description(bookListDto.getDescription())
                .build();
    }
}
