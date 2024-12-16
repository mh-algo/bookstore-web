package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.common.repository.BookProductRepository;
import com.bookshelf.bookproject.common.repository.ImagesRepository;
import com.bookshelf.bookproject.publicpage.repository.dto.BookDetailDto;
import com.bookshelf.bookproject.publicpage.service.dto.BookDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bookshelf.bookproject.publicpage.BookServiceUtil.*;

@Service
@RequiredArgsConstructor
public class BookDetailService {
    private final BookProductRepository bookProductRepository;
    private final ImagesRepository imagesRepository;

    @Transactional(readOnly = true)
    public BookDetail getBookDetailInfo(String bookId) {
        return createBookDetail(getBookDetailById(bookId), getBookSubImages(bookId));
    }

    private BookDetailDto getBookDetailById(String bookId) {
        return bookProductRepository.findBookDetailById(stringToLongId(bookId));
    }

    // 판매자가 저장한 추가 이미지 가져오기
    public List<String> getBookSubImages(String bookId) {
        return imagesRepository.findSubImageUrlByBookProductId(stringToLongId(bookId));
    }

    private BookDetail createBookDetail(BookDetailDto bookDetailDto, List<String> subImages) {
        String imageUrl = getImageUrl(bookDetailDto.getImageUrl(), bookDetailDto.getDefaultImageUrl());
        Integer discount = bookDetailDto.getDiscount();
        Integer price = getPrice(bookDetailDto.getPrice(), bookDetailDto.getCustomPrice());
        int discountPrice = calculateDiscountPrice(price, discount);
        String discountRate = calculateDiscountRate(price, discount);

        return BookDetail.builder()
                .id(bookDetailDto.getId())
                .title(bookDetailDto.getTitle())
                .subtitle(bookDetailDto.getSubtitle())
                .imageUrl(imageUrl)
                .author(bookDetailDto.getAuthor())
                .publisher(bookDetailDto.getPublisher())
                .publishedDate(bookDetailDto.getPublishedDate())
                .discountPrice(discountPrice)
                .discountRate(discountRate)
                .description(bookDetailDto.getDescription())
                .price(price)
                .stock(bookDetailDto.getStock())
                .deliveryFee(bookDetailDto.getDeliveryFee())
                .isbn(bookDetailDto.getIsbn())
                .category(bookDetailDto.getCategory())
                .subcategory(bookDetailDto.getSubcategory())
                .subSubcategory(bookDetailDto.getSubSubcategory())
                .subSubcategoryId(bookDetailDto.getSubSubcategoryId())
                .subImagesUrl(subImages)
                .build();
    }
}
