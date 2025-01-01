package com.bookshelf.bookproject.publicpage.controller;

import com.bookshelf.bookproject.common.ApiResponse;
import com.bookshelf.bookproject.publicpage.service.BookDetailService;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewLike;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewList;
import com.bookshelf.bookproject.security.dto.AccountAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        Page<ReviewList> reviewPage = bookDetailService.getReviewList(pageable, bookId, getAccountId(accountAuth));
        return ResponseEntity.ok(ApiResponse.success(reviewPage));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Map<String, String>>> updateReview(@PathVariable Long bookId,
                                                            @PathVariable Long reviewId,
                                                            @RequestBody Map<String, String> payload,
                                                            @AuthenticationPrincipal AccountAuth accountAuth) {
        String updatedContext = bookDetailService.updateReview(bookId, reviewId, payload.get("context"), accountAuth);
        return ResponseEntity.ok(ApiResponse.success("리뷰가 수정되었습니다.", Map.of("context", updatedContext)));
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
                                                              @RequestBody Map<String, Long> payload,
                                                              @AuthenticationPrincipal AccountAuth accountAuth) {
        ReviewLike reviewId = bookDetailService.toggleLike(payload.get("reviewId"), accountAuth);
        return ResponseEntity.ok(ApiResponse.success(reviewId));
    }

    @PostMapping("/cart")
    public ResponseEntity<ApiResponse<Void>> addBookToCart(@PathVariable Long bookId,
                                                             @RequestBody Map<String, Integer> payload,
                                                             @AuthenticationPrincipal AccountAuth accountAuth) {
        bookDetailService.saveBookToCart(bookId, payload.get("quantity"), accountAuth);
        return ResponseEntity.ok(ApiResponse.success("장바구니에 저장했습니다."));
    }
}
