package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.common.repository.BookProductRepository;
import com.bookshelf.bookproject.common.repository.ImagesRepository;
import com.bookshelf.bookproject.domain.BookProduct;
import com.bookshelf.bookproject.publicpage.repository.dto.BookDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDetailCache {
    private final BookProductRepository bookProductRepository;
    private final ImagesRepository imagesRepository;

    @Cacheable(value = "bookList:detail", key = "#id", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public BookDetailDto getBookDetailById(Long id) {
        return bookProductRepository.findBookDetailById(id);
    }

    @Cacheable(value = "bookList:subImages", key = "#bookId", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public List<String> getBookSubImages(Long bookId) {
        return imagesRepository.findSubImageUrlByBookProductId(bookId);
    }

    @Cacheable(value = "bookList:bookProduct", key = "#id", cacheResolver = "cacheResolver")
    @Transactional(readOnly = true)
    public BookProduct getBookProduct(Long id) {
        return bookProductRepository.findById(id).orElse(BookProduct.empty());
    }
}
