package com.bookshelf.bookproject.seller.service;

import com.bookshelf.bookproject.domain.Book;
import com.bookshelf.bookproject.seller.repository.BookRepository;
import com.bookshelf.bookproject.seller.service.dto.BookInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ManagementCache {
    private final BookRepository bookRepository;

    @Cacheable(value = "bookData", key = "#bookInfo.isbn", cacheManager = "cacheManagerWith24Hours")
    @Transactional
    public Book findOrSaveBook(BookInfo bookInfo) {
        return bookRepository.findByIsbn(bookInfo.getIsbn())
                .orElseGet(() -> bookRepository.save(createBook(bookInfo)));
    }

    private static Book createBook(BookInfo bookInfo) {
        return Book.builder()
                .title(bookInfo.getTitle())
                .imageUrl(bookInfo.getImage())
                .author(bookInfo.getAuthor())
                .publisher(bookInfo.getPublisher())
                .isbn(bookInfo.getIsbn())
                .publishedDate(getLocalDate(bookInfo.getPubdate()))
                .price(bookInfo.getDiscount())
                .description(bookInfo.getDescription())
                .build();
    }

    private static LocalDate getLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(date, formatter);
    }
}
