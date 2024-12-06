package com.bookshelf.bookproject.publics.service;

import com.bookshelf.bookproject.common.repository.BookProductRepository;
import com.bookshelf.bookproject.publics.repository.dto.BookListDto;
import com.bookshelf.bookproject.publics.service.dto.BookListInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookListService {
    private final BookProductRepository bookProductRepository;

    public List<BookListInfo> getAllBooksList() {
        List<BookListDto> allBookProducts = bookProductRepository.findAllBookProducts();
        return allBookProducts.stream()
                .map(BookListService::createBookList).toList();
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
