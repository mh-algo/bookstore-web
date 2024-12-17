package com.bookshelf.bookproject.seller.service;

import com.bookshelf.bookproject.common.AccountCache;
import com.bookshelf.bookproject.domain.Account;
import com.bookshelf.bookproject.domain.Book;
import com.bookshelf.bookproject.seller.service.dto.TitleDto;
import com.bookshelf.bookproject.seller.repository.BookRepository;
import com.bookshelf.bookproject.seller.service.dto.BookInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ManagementCache {
    private final BookRepository bookRepository;
    private final AccountCache accountCache;

    @Cacheable(value = "seller:bookIsbn", key = "#isbn", unless = "#result.empty", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public Book findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseGet(() -> Book.builder().build());
    }

    @Cacheable(value = "seller:bookIsbn", key = "#bookInfo.isbn", cacheResolver = "cacheResolver")
    @Transactional
    public Book findOrSaveBook(BookInfo bookInfo) {
        return bookRepository.findByIsbn(bookInfo.getIsbn())
                .orElseGet(() -> bookRepository.save(createBook(bookInfo)));
    }

    private static Book createBook(BookInfo bookInfo) {
        TitleDto titleDto = splitTitleAndSubtitle(bookInfo.getTitle());
        return Book.builder()
                .title(titleDto.getTitle())
                .subtitle(titleDto.getSubtitle())
                .imageUrl(bookInfo.getImage())
                .author(getAuthor(bookInfo.getAuthor()))
                .publisher(bookInfo.getPublisher())
                .isbn(bookInfo.getIsbn())
                .publishedDate(getLocalDate(bookInfo.getPubdate()))
                .price(bookInfo.getDiscount())
                .description(bookInfo.getDescription())
                .build();
    }

    private static TitleDto splitTitleAndSubtitle(String text) {
        String regex = "\\((.*?)\\)$";
        Matcher matcher = Pattern.compile(regex).matcher(text);

        boolean isMatched = matcher.find();
        String title = isMatched ? text.replaceFirst(regex, "").strip() : text;
        String subTitle = isMatched ? matcher.group(1) : "";

        return new TitleDto(title, subTitle);
    }

    private static String getAuthor(String author) {
        return author.contains("^") ? author.replace("^", "/") : author;
    }

    private static LocalDate getLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(date, formatter);
    }

    public Account getAccount(String accountId) {
        return accountCache.getAccount(accountId);
    }
}
