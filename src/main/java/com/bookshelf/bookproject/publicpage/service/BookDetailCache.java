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

import static com.bookshelf.bookproject.config.CacheConstants.BOOK_LIST;
import static com.bookshelf.bookproject.config.CacheConstants.CACHE_RESOLVER;

@Service
@RequiredArgsConstructor
public class BookDetailCache {
    private final BookProductRepository bookProductRepository;
    private final ImagesRepository imagesRepository;

    @Cacheable(value = BOOK_LIST + ":detail", key = "#id", cacheResolver = CACHE_RESOLVER)
    @Transactional(readOnly = true)
    public BookDetailDto getBookDetailById(Long id) {
        return bookProductRepository.findBookDetailById(id);
    }

    @Cacheable(value = BOOK_LIST + ":subImages", key = "#bookId", cacheResolver = CACHE_RESOLVER)
    @Transactional(readOnly = true)
    public List<String> getBookSubImages(Long bookId) {
        return imagesRepository.findSubImageUrlByBookProductId(bookId);
    }

    @Cacheable(value = BOOK_LIST + ":bookProduct", key = "#id", cacheResolver = CACHE_RESOLVER)
    @Transactional(readOnly = true)
    public BookProduct getBookProduct(Long id) {
        return bookProductRepository.findById(id).orElse(BookProduct.empty());
    }
}
