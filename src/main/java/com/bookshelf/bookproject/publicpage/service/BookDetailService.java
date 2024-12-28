package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.common.AccountCache;
import com.bookshelf.bookproject.common.CustomPage;
import com.bookshelf.bookproject.publicpage.repository.ReviewRepository;
import com.bookshelf.bookproject.domain.Account;
import com.bookshelf.bookproject.domain.BookProduct;
import com.bookshelf.bookproject.domain.Review;
import com.bookshelf.bookproject.publicpage.controller.dto.ReviewData;
import com.bookshelf.bookproject.publicpage.repository.dto.BookDetailDto;
import com.bookshelf.bookproject.publicpage.service.dto.BookDetail;
import com.bookshelf.bookproject.publicpage.repository.dto.ReviewListDto;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewLike;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewList;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.bookshelf.bookproject.config.CacheConstants.CACHE_RESOLVER;
import static com.bookshelf.bookproject.config.CacheConstants.REVIEW;
import static com.bookshelf.bookproject.publicpage.BookServiceUtil.*;

@Service
@RequiredArgsConstructor
public class BookDetailService {
    private final BookDetailCache bookDetailCache;
    private final AccountCache accountCache;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeCache reviewLikeCache;
    private final ReviewCache reviewCache;

    private static final int PAGE_SIZE = 10;

    public BookDetail getBookDetailInfo(String bookId) {
        return createBookDetail(getBookDetailById(bookId), getBookSubImages(bookId));
    }

    private BookDetailDto getBookDetailById(String bookId) {
        return bookDetailCache.getBookDetailById(stringToLongId(bookId));
    }

    // 판매자가 저장한 추가 이미지 가져오기
    public List<String> getBookSubImages(String bookId) {
        return bookDetailCache.getBookSubImages(stringToLongId(bookId));
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
    @CacheEvict(value = REVIEW + ":#{#bookId}", allEntries = true, cacheResolver = CACHE_RESOLVER)
    @Transactional
    public void registerReview(ReviewData reviewData, String accountId, String bookId) {
        Review review = createReview(reviewData, getAccount(accountId), getBookProduct(bookId));
        reviewRepository.save(review);
    }

    private Account getAccount(String accountId) {
        return accountCache.getAccount(accountId);
    }

    private BookProduct getBookProduct(String bookId) {
        return bookDetailCache.getBookProduct(stringToLongId(bookId));
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

    public Page<ReviewList> getReviewList(Pageable pageable, String bookId, String accountId) {
        Page<ReviewListDto> page = getReviewList(pageable, bookId);
        Account account = getAccount(accountId);

        List<ReviewList> reviewList = (account == null)
                ? page.getContent().stream()
                        .map(this::createReviewList)
                        .toList()
                : page.getContent().stream()
                        .map(reviewInfo -> createReviewList(
                                reviewInfo,
                                getLikeStatusSet(bookId, account.getId()),
                                accountId)
                        ).toList();

        return new CustomPage<>(new PageImpl<>(reviewList, page.getPageable(), page.getTotalElements()));
    }

    private Page<ReviewListDto> getReviewList(Pageable pageable, String bookId) {
        return reviewCache.getReviewList(createRequestPageable(pageable), stringToLongId(bookId));
    }

    private static Pageable createRequestPageable(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());
    }

    private Set<Long> getLikeStatusSet(String bookId, Long accountEntityId) {
        return reviewLikeCache.getLikeStatusSet(stringToLongId(bookId), accountEntityId);
    }

    private ReviewList createReviewList(ReviewListDto reviewInfo) {
        return ReviewList.builder()
                .id(reviewInfo.getId())
                .accountId(reviewInfo.getAccountId())
                .rating(reviewInfo.getRating())
                .context(reviewInfo.getContext())
                .likeCount(getLikeCount(reviewInfo))
                .likeStatus(false)
                .createdDate(reviewInfo.getCreatedDate())
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
                .createdDate(reviewInfo.getCreatedDate())
                .build();
    }

    private int getLikeCount(ReviewListDto review) {
        return reviewLikeCache.getValidLikeCount(review.getId(), review.getLikeCount());
    }

    private boolean getLikeStatus(Set<Long> likeStatusSet, Long reviewId, String accountId) {
        boolean liked = likeStatusSet.contains(reviewId);
        return reviewLikeCache.getValidLikeStatus(reviewId, accountId, liked);
    }

    @PreAuthorize("isAuthenticated() and #accountId == authentication.name")
    public ReviewLike toggleLike(Long reviewId, String accountId) {
        Long id = getAccount(accountId).getId();
        boolean liked = reviewLikeCache.isLiked(reviewId, id, accountId);
        int likeCount = reviewLikeCache.getLikeCount(reviewId);

        synchronized (this) {
            liked = reviewLikeCache.updateLikeStatus(reviewId, accountId, liked);
            likeCount = reviewLikeCache.updateLikeCount(reviewId, liked, likeCount);
        }
        reviewLikeCache.saveCacheKeys(reviewId, accountId);

        return new ReviewLike(liked, likeCount);
    }
}
