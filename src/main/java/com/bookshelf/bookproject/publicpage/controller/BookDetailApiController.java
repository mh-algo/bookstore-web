package com.bookshelf.bookproject.publicpage.controller;

import com.bookshelf.bookproject.common.ApiResponse;
import com.bookshelf.bookproject.common.exception.UnAuthenticationException;
import com.bookshelf.bookproject.publicpage.service.BookDetailService;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewLike;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewList;
import com.bookshelf.bookproject.security.dto.AccountAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewList>>> loadReviewPage(@PathVariable Long bookId,
                                                                        @AuthenticationPrincipal AccountAuth accountAuth,
                                                                        Pageable pageable) {
        Page<ReviewList> reviewPage = bookDetailService.getReviewList(pageable, bookId, getAccountId(accountAuth));
        if (reviewPage.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(
                            HttpStatus.NOT_FOUND.value(),
                            "Review with page " + (pageable.getPageNumber()+1) + " not found.",
                            reviewPage)
            );
        }
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", reviewPage));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<String>> updateReview(@PathVariable Long bookId,
                                                            @PathVariable Long reviewId,
                                                            @RequestBody Map<String, String> payload,
                                                            @AuthenticationPrincipal AccountAuth accountAuth) {
        if (accountAuth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Authentication token is missing or invalid.", null));
        }

        String context = payload.get("context");
        String updatedContext = bookDetailService.updateReview(bookId, reviewId, context, getAccountId(accountAuth));
        if (updatedContext == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "Access Denied", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", updatedContext));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable Long bookId,
                                                            @PathVariable Long reviewId,
                                                            @AuthenticationPrincipal AccountAuth accountAuth) {
        if (accountAuth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Authentication token is missing or invalid."));
        }

        boolean deleted = bookDetailService.deleteReview(bookId, reviewId, getAccountId(accountAuth));
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "Access Denied"));
        }

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success"));
    }

    @PatchMapping("/reviews/like")
    public ResponseEntity<ApiResponse<ReviewLike>> likeReview(@PathVariable String bookId,
                                                              @RequestBody Map<String, Long> payload,
                                                              @AuthenticationPrincipal AccountAuth accountAuth) {
        if (accountAuth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Authentication token is missing or invalid."));
        }
        ReviewLike reviewId = bookDetailService.toggleLike(payload.get("reviewId"), getAccountId(accountAuth));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", reviewId));
    }

    @PostMapping("/cart")
    public ResponseEntity<String> addBookToCart(@PathVariable Long bookId,
                                                             @RequestBody Map<String, Integer> payload,
                                                             @AuthenticationPrincipal AccountAuth accountAuth) {
        if (accountAuth == null) {
            throw new UnAuthenticationException("로그인 후 시도해주세요.");
        }
        bookDetailService.saveBookToCart(bookId, payload.get("quantity"), getAccountId(accountAuth));
        return ResponseEntity.ok("장바구니에 저장했습니다.");
    }
}
