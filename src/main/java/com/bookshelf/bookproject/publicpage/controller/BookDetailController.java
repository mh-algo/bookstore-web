package com.bookshelf.bookproject.publicpage.controller;

import com.bookshelf.bookproject.publicpage.controller.dto.ReviewData;
import com.bookshelf.bookproject.publicpage.service.BookDetailService;
import com.bookshelf.bookproject.publicpage.service.dto.BookDetail;
import com.bookshelf.bookproject.publicpage.service.dto.ReviewList;
import com.bookshelf.bookproject.security.dto.AccountAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookDetailController {
    private final BookDetailService bookDetailService;

    @GetMapping("/{bookId}")
    public String bookDetail(@PathVariable String bookId, @ModelAttribute ReviewData reviewData, Model model,
                             @AuthenticationPrincipal AccountAuth accountAuth) {
        addBookDetail(bookId, model);

        List<ReviewList> reviewList = bookDetailService.getReviewList(bookId, getAccountId(accountAuth));
        model.addAttribute("reviewList", reviewList);
        return "public-page/book-detail";
    }

    private void addBookDetail(String bookId, Model model) {
        BookDetail bookDetail = bookDetailService.getBookDetailInfo(bookId);
        model.addAttribute("bookDetail", bookDetail);
    }

    private static String getAccountId(AccountAuth accountAuth) {
        return accountAuth == null ? "" : accountAuth.getAccountId();
    }

    @PostMapping("/{bookId}/review")
    public String saveReview(@PathVariable String bookId,
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
