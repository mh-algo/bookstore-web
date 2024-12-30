package com.bookshelf.bookproject.publicpage.controller;

import com.bookshelf.bookproject.publicpage.controller.dto.ReviewData;
import com.bookshelf.bookproject.publicpage.service.BookDetailService;
import com.bookshelf.bookproject.publicpage.service.dto.BookDetail;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewList;
import com.bookshelf.bookproject.security.dto.AccountAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.bookshelf.bookproject.publicpage.BookServiceUtils.getAccountId;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookDetailController {
    private final BookDetailService bookDetailService;

    @GetMapping("/{bookId}")
    public String bookDetail(@PathVariable Long bookId, @ModelAttribute ReviewData reviewData, Model model,
                             @AuthenticationPrincipal AccountAuth accountAuth,
                             Pageable pageable) {
        addBookDetail(bookId, model);

        String accountId = getAccountId(accountAuth);
        Page<ReviewList> reviewPage = bookDetailService.getReviewList(pageable, bookId, accountId);
        model.addAttribute("reviewPage", reviewPage);
        return "public-page/book-detail";
    }

    private void addBookDetail(Long bookId, Model model) {
        BookDetail bookDetail = bookDetailService.getBookDetailInfo(bookId);
        model.addAttribute("bookDetail", bookDetail);
    }

    @PostMapping("/{bookId}/reviews")
    public String saveReview(@PathVariable Long bookId,
                             @Valid @ModelAttribute ReviewData reviewData,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal AccountAuth accountAuth) {
        if (accountAuth == null) {
            return "redirect:/login";
        }

        if (!bindingResult.hasErrors()) {
            bookDetailService.registerReview(reviewData, accountAuth.getAccountId(), bookId);
        }

        return "redirect:/books/" + bookId;
    }
}
