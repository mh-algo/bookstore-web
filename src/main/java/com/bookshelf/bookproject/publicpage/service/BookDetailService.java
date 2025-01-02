package com.bookshelf.bookproject.publicpage.service;

import com.bookshelf.bookproject.common.AccountCache;
import com.bookshelf.bookproject.common.CustomPage;
import com.bookshelf.bookproject.common.exception.AccessDeniedException;
import com.bookshelf.bookproject.common.exception.SaveFailedException;
import com.bookshelf.bookproject.common.exception.UnAuthenticationException;
import com.bookshelf.bookproject.common.repository.BookProductRepository;
import com.bookshelf.bookproject.domain.*;
import com.bookshelf.bookproject.publicpage.repository.CartProductRepository;
import com.bookshelf.bookproject.publicpage.repository.CartRepository;
import com.bookshelf.bookproject.publicpage.repository.ReviewRepository;
import com.bookshelf.bookproject.publicpage.controller.dto.bookdetail.ReviewData;
import com.bookshelf.bookproject.publicpage.repository.dto.BookDetailDto;
import com.bookshelf.bookproject.publicpage.service.dto.BookDetail;
import com.bookshelf.bookproject.publicpage.repository.dto.ReviewListDto;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewLike;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewList;
import com.bookshelf.bookproject.security.dto.AccountAuth;
import jakarta.persistence.EntityManager;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.bookshelf.bookproject.config.CacheConstants.CACHE_RESOLVER;
import static com.bookshelf.bookproject.config.CacheConstants.REVIEW;
import static com.bookshelf.bookproject.publicpage.BookServiceUtils.*;

@Service
@RequiredArgsConstructor
public class BookDetailService {
    private final BookDetailCache bookDetailCache;
    private final AccountCache accountCache;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeCache reviewLikeCache;
    private final ReviewCache reviewCache;
    private final CartRepository cartRepository;
    private final BookProductRepository bookProductRepository;
    private final CartProductRepository cartProductRepository;
    private final EntityManager em;

    private static final int PAGE_SIZE = 10;

    public BookDetail getBookDetailInfo(Long bookId) {
        return createBookDetail(getBookDetailById(bookId), getBookSubImages(bookId));
    }

    private BookDetailDto getBookDetailById(Long bookId) {
        return bookDetailCache.getBookDetailById(bookId);
    }

    // 판매자가 저장한 추가 이미지 가져오기
    public List<String> getBookSubImages(Long bookId) {
        return bookDetailCache.getBookSubImages(bookId);
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

    // 리뷰 등록
    @PreAuthorize("isAuthenticated() and #accountId == authentication.name")
    @CacheEvict(value = REVIEW + ":#{#bookId}", allEntries = true, cacheResolver = CACHE_RESOLVER)
    @Transactional
    public void registerReview(ReviewData reviewData, AccountAuth accountAuth, Long bookId) {
        validateAuthentication(accountAuth);
        Review review = createReview(reviewData, getAccount(accountAuth.getAccountId()), getBookProduct(bookId));
        reviewRepository.save(review);
    }

    private Account getAccount(String accountId) {
        return accountCache.getAccount(accountId);
    }

    private BookProduct getBookProduct(Long bookId) {
        return bookDetailCache.getBookProduct(bookId);
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

    // 리뷰 조회
    public Page<ReviewList> getReviewList(Pageable pageable, Long bookId, String accountId) {
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

        if (reviewList.isEmpty()) {
            throw new IllegalArgumentException("Review with page " + (pageable.getPageNumber()+1) + " not found.");
        }

        return new CustomPage<>(new PageImpl<>(reviewList, page.getPageable(), page.getTotalElements()));
    }

    private Page<ReviewListDto> getReviewList(Pageable pageable, Long bookId) {
        return reviewCache.getReviewList(createRequestPageable(pageable), bookId);
    }

    private static Pageable createRequestPageable(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, pageable.getSort());
    }

    private Set<Long> getLikeStatusSet(Long bookId, Long accountEntityId) {
        return reviewLikeCache.getLikeStatusSet(bookId, accountEntityId);
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
                .reviewOwner(false)
                .build();
    }

    private ReviewList createReviewList(ReviewListDto reviewInfo, Set<Long> likeStatusSet, String accountId) {
        Long id = reviewInfo.getId();
        String reviewAccountId = reviewInfo.getAccountId();
        return ReviewList.builder()
                .id(id)
                .accountId(reviewAccountId)
                .rating(reviewInfo.getRating())
                .context(reviewInfo.getContext())
                .likeCount(getLikeCount(reviewInfo))
                .likeStatus(getLikeStatus(likeStatusSet, id, accountId))
                .createdDate(reviewInfo.getCreatedDate())
                .reviewOwner(isReviewOwner(reviewAccountId, accountId))
                .build();
    }

    private int getLikeCount(ReviewListDto review) {
        return reviewLikeCache.getValidLikeCount(review.getId(), review.getLikeCount());
    }

    private boolean getLikeStatus(Set<Long> likeStatusSet, Long reviewId, String accountId) {
        boolean liked = likeStatusSet.contains(reviewId);
        return reviewLikeCache.getValidLikeStatus(reviewId, accountId, liked);
    }

    private static boolean isReviewOwner(String reviewAccountId, String accountId) {
        return Objects.equals(reviewAccountId, accountId);
    }

    // 좋아요 버튼 누름
    @PreAuthorize("isAuthenticated() and #accountId == authentication.name")
    public ReviewLike toggleLike(Long reviewId, AccountAuth accountAuth) {
        validateAuthentication(accountAuth);
        String accountId = accountAuth.getAccountId();
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

    @PreAuthorize("isAuthenticated() and #accountId == authentication.name")
    @CacheEvict(value = REVIEW + ":#{#bookId}", allEntries = true, cacheResolver = CACHE_RESOLVER)
    @Transactional
    public String updateReview(Long bookId, Long reviewId, String context, AccountAuth accountAuth) {
        validateAuthentication(accountAuth);
        Review review = reviewRepository.findById(reviewId).orElseGet(Review::empty);
        if (validateReviewId(reviewId, review) && validateReviewOwner(accountAuth.getAccountId(), review)) {
            review.updateContext(context);
            return review.getContext();
        } else {
            throw new AccessDeniedException("Access Denied");
        }
    }

    @PreAuthorize("isAuthenticated() and #accountId == authentication.name")
    @CacheEvict(value = REVIEW + ":#{#bookId}", allEntries = true, cacheResolver = CACHE_RESOLVER)
    @Transactional
    public void deleteReview(Long bookId, Long reviewId, AccountAuth accountAuth) {
        validateAuthentication(accountAuth);
        Review review = reviewRepository.findById(reviewId).orElseGet(Review::empty);
        if (validateReviewId(reviewId, review) && validateReviewOwner(accountAuth.getAccountId(), review)) {
            em.remove(review);
        } else {
            throw new AccessDeniedException("Access Denied");
        }
    }

    private static boolean validateReviewId(Long reviewId, Review review) {
        return Objects.equals(review.getId(), reviewId);
    }

    private boolean validateReviewOwner(String accountId, Review review) {
        return Objects.equals(review.getAccount().getId(), getAccount(accountId).getId());
    }

    @PreAuthorize("isAuthenticated() and #accountId == authentication.name")
    @Transactional
    public void saveBookToCart(Long bookId, Integer quantity, AccountAuth accountAuth) {
        validateAuthentication(accountAuth);
        if (getAccount(accountAuth.getAccountId()) instanceof User user) {
            CartProduct cartProduct = createCartProduct(bookId, quantity, createCart(user));
            if (cartProduct.isEmpty()) {
                throw new SaveFailedException("저장 실패");
            }
        } else {
            throw new AccessDeniedException("판매 회원이 아닌 일반 회원만 이용할 수 있습니다.");
        }
    }

    private CartProduct createCartProduct(Long bookId, Integer quantity, Cart cart) {
        Optional<BookProduct> bookProduct = bookProductRepository.findById(bookId);
        if (bookProduct.isEmpty()) {
            throw new SaveFailedException("저장 실패");
        }

        CartProduct cartProduct = new CartProduct(cart, bookProduct.get(), quantity);
        return cartProductRepository.save(cartProduct);
    }

    private Cart createCart(User user) {
        Cart cart = cartRepository.save(new Cart(user));
        if (cart.isEmpty()) {
            throw new SaveFailedException("저장 실패");
        }
        return cart;
    }

    private static void validateAuthentication(AccountAuth accountAuth) {
        if (accountAuth == null) {
            throw new UnAuthenticationException("로그인 후 시도해주세요.");
        }
    }
}
