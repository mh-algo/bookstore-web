package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.common.AccountCache;
import com.bookshelf.bookproject.common.repository.BookProductRepository;
import com.bookshelf.bookproject.common.repository.ImagesRepository;
import com.bookshelf.bookproject.publicpage.repository.LikeStatusRepository;
import com.bookshelf.bookproject.publicpage.repository.ReviewRepository;
import com.bookshelf.bookproject.domain.Account;
import com.bookshelf.bookproject.domain.BookProduct;
import com.bookshelf.bookproject.domain.Review;
import com.bookshelf.bookproject.publicpage.controller.dto.ReviewData;
import com.bookshelf.bookproject.publicpage.repository.dto.BookDetailDto;
import com.bookshelf.bookproject.publicpage.service.dto.BookDetail;
import com.bookshelf.bookproject.publicpage.repository.dto.ReviewListDto;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.bookshelf.bookproject.publicpage.BookServiceUtil.*;

@Service
@RequiredArgsConstructor
public class BookDetailService {
    private final BookProductRepository bookProductRepository;
    private final ImagesRepository imagesRepository;
    private final AccountCache accountCache;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeCache reviewLikeCache;
    private final LikeStatusRepository likeStatusRepository;

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

    @PreAuthorize("isAuthenticated() and #accountId == authentication.name")
    @Transactional
    public void registerReview(ReviewData reviewData, String accountId, String bookId) {
        Review review = createReview(reviewData, getAccount(accountId), getBookProduct(bookId));
        reviewRepository.save(review);
    }

    private Account getAccount(String accountId) {
        return accountCache.getAccount(accountId);
    }

    private BookProduct getBookProduct(String bookId) {
        return bookProductRepository.findByBookId(stringToLongId(bookId));
    }

    private static Review createReview(ReviewData reviewData, Account account, BookProduct bookProduct) {
        return Review.builder()
                .account(account)
                .bookProduct(bookProduct)
                .context(reviewData.getContext())
                .rating(reviewData.getRating())
                .likeCount(0)
                .build();
    }

    @Transactional(readOnly = true)
    public List<ReviewList> getReviewList(String bookId, String accountId) {
        long bookIdAsLong = stringToLongId(bookId);
        List<ReviewListDto> reviewList = reviewRepository.findReviewListByBookId(bookIdAsLong);

        if (accountId.isEmpty()) {
            return reviewList.stream().map(this::createReviewList).toList();
        } else {
            Set<Long> likeStatusSet = likeStatusRepository.findReviewIdByBookProductId(bookIdAsLong);
            return reviewList.stream().map(reviewInfo ->
                    createReviewList(reviewInfo, likeStatusSet, accountId)
            ).toList();
        }
    }

    private ReviewList createReviewList(ReviewListDto reviewInfo) {
        return ReviewList.builder()
                .id(reviewInfo.getId())
                .accountId(reviewInfo.getAccountId())
                .rating(reviewInfo.getRating())
                .context(reviewInfo.getContext())
                .likeCount(getLikeCount(reviewInfo))
                .likeStatus(false)
                .build();
    }

    private ReviewList createReviewList(ReviewListDto reviewInfo, Set<Long> likeStatusSet, String accountId) {
        Long id = reviewInfo.getId();
        return ReviewList.builder()
                .id(id)
                .accountId(reviewInfo.getAccountId())
                .rating(reviewInfo.getRating())
                .context(reviewInfo.getContext())
                .likeCount(getLikeCount(reviewInfo))
                .likeStatus(getLikeStatus(likeStatusSet, id, accountId))
                .build();
    }

    private int getLikeCount(ReviewListDto review) {
        return reviewLikeCache.getValidLikeCount(review.getId(), review.getLikeCount());
    }

    private boolean getLikeStatus(Set<Long> likeStatusSet, Long reviewId, String accountId) {
        boolean liked = likeStatusSet.contains(reviewId);
        return reviewLikeCache.getValidLikeStatus(reviewId, accountId, liked);
    }

//    public ReviewLike toggleLike(Long reviewId, String accountId) {
//        boolean liked = !reviewLikeCache.isLiked(reviewId, accountId);  // true -> false, false -> true 변환
//        int likeCount = reviewLikeCache.getLikeCount(reviewId);
//
//        synchronized (this) {
//            reviewLikeCache.updateLikeStatus(reviewId, accountId, liked);
//            reviewLikeCache.updateLikeCount(reviewId, liked, likeCount);
//        }
//        return new ReviewLike(liked, likeCount);
//    }
}
