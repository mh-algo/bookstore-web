package com.bookshelf.bookproject.publics.service;

import com.bookshelf.bookproject.common.CustomPage;
import com.bookshelf.bookproject.common.repository.BookProductRepository;
import com.bookshelf.bookproject.publics.repository.dto.BookListDto;
import com.bookshelf.bookproject.publics.service.dto.BookListInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookListService {
    private static final int PAGE_SIZE = 10;
    private final BookProductRepository bookProductRepository;

    public Page<BookListInfo> getBooksPage(Pageable pageable) {
        Page<BookListDto> page = bookProductRepository.findPageBookProducts(createRequestPageable(pageable));
        List<BookListInfo> bookList = page.getContent().stream()
                .map(BookListService::createBookList).toList();

        return new CustomPage<>(new PageImpl<>(bookList, page.getPageable(), page.getTotalElements()));
    }

    private static Pageable createRequestPageable(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());
    }

    private static BookListInfo createBookList(BookListDto bookListDto) {
        String imageUrl = getImageUrl(bookListDto.getImageUrl(), bookListDto.getDefaultImageUrl());
        Integer discount = bookListDto.getDiscount();
        Integer price = getPrice(bookListDto.getPrice(), bookListDto.getCustomPrice());
        int discountPrice = calculateDiscountPrice(price, discount);
        String discountRate = calculateDiscountRate(price, discount);

        return BookListInfo.builder()
                .id(bookListDto.getId())
                .title(bookListDto.getTitle())
                .imageUrl(imageUrl)
                .author(bookListDto.getAuthor())
                .publisher(bookListDto.getPublisher())
                .publishedDate(bookListDto.getPublishedDate())
                .price(discountPrice)
                .discountRate(discountRate)
                .description(bookListDto.getDescription())
                .build();
    }

    private static String getImageUrl(String imageUrl, String defaultImageUrl) {
        return imageUrl == null || imageUrl.isBlank() ? defaultImageUrl : imageUrl;
    }

    private static Integer getPrice(Integer price, Integer customPrice) {
        return price == null || price == 0 ? customPrice : price;
    }

    private static int calculateDiscountPrice(Integer price, Integer discount) {
        if (price == null || discount == null) {
            return 0;
        }
        return Math.max(price - discount, 0);
    }

    private static String calculateDiscountRate(Integer price, Integer discount) {
        if (price == null || price == 0 || discount == null || discount == 0) {
            return "0";
        }
        double discountRate = (double) discount / price * 100;
        return String.format("%.1f", discountRate);
    }
}
