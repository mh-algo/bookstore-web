package com.bookshelf.bookproject.publicpage.controller;

import com.bookshelf.bookproject.common.ApiResponse;
import com.bookshelf.bookproject.publicpage.controller.dto.bookdetail.ReviewContext;
import com.bookshelf.bookproject.publicpage.controller.dto.bookdetail.ReviewData;
import com.bookshelf.bookproject.publicpage.controller.dto.bookdetail.ReviewId;
import com.bookshelf.bookproject.publicpage.controller.dto.bookdetail.ReviewQuantity;
import com.bookshelf.bookproject.publicpage.service.BookDetailService;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewLike;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewList;
import com.bookshelf.bookproject.security.dto.AccountAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.bookshelf.bookproject.publicpage.BookServiceUtils.getAccountId;

@RestController
@RequestMapping("/books/{bookId}")
@RequiredArgsConstructor
public class BookDetailApiController {
    private final BookDetailService bookDetailService;

    @GetMapping(value = "/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewList>>> loadReviewPage(@PathVariable Long bookId,
                                                                        @AuthenticationPrincipal AccountAuth accountAuth,
                                                                        Pageable pageable) {
        Page<ReviewList> reviewPage = bookDetailService.getReviewListApi(pageable, bookId, getAccountId(accountAuth));
        return ResponseEntity.ok(ApiResponse.success(reviewPage));
    }

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse<Void>> saveReview(@PathVariable Long bookId,
                             @Valid @RequestBody ReviewData reviewData,
                             @AuthenticationPrincipal AccountAuth accountAuth) {
        bookDetailService.registerReview(reviewData, accountAuth, bookId);
        return ResponseEntity.ok(ApiResponse.success("리뷰가 등록되었습니다."));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewContext>> updateReview(@PathVariable Long bookId,
                                                            @PathVariable Long reviewId,
                                                            @Valid @RequestBody ReviewContext review,
                                                            @AuthenticationPrincipal AccountAuth accountAuth) {
        String updatedContext = bookDetailService.updateReview(bookId, reviewId, review.getContext(), accountAuth);
        return ResponseEntity.ok(ApiResponse.success("리뷰가 수정되었습니다.", ReviewContext.of(updatedContext)));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long bookId,
                                                          @PathVariable Long reviewId,
                                                          @AuthenticationPrincipal AccountAuth accountAuth) {
        bookDetailService.deleteReview(bookId, reviewId, accountAuth);
        return ResponseEntity.ok(ApiResponse.success("리뷰가 삭제되었습니다."));
    }

    @PatchMapping("/reviews/like")
    public ResponseEntity<ApiResponse<ReviewLike>> likeReview(@PathVariable String bookId,
                                                              @Valid @RequestBody ReviewId review,
                                                              @AuthenticationPrincipal AccountAuth accountAuth) {
        ReviewLike reviewId = bookDetailService.toggleLike(review.getReviewId(), accountAuth);
        return ResponseEntity.ok(ApiResponse.success(reviewId));
    }

    @PostMapping("/cart")
    public ResponseEntity<ApiResponse<Void>> addBookToCart(@PathVariable Long bookId,
                                                           @Valid @RequestBody ReviewQuantity review,
                                                           @AuthenticationPrincipal AccountAuth accountAuth) {
        bookDetailService.saveBookToCart(bookId, review.getQuantity(), accountAuth);
        return ResponseEntity.ok(ApiResponse.success("장바구니에 저장했습니다."));
    }
}
