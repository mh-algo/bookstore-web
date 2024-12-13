package com.bookshelf.bookproject.publicpage.controller;

import com.bookshelf.bookproject.publicpage.service.BookDetailService;
import com.bookshelf.bookproject.publicpage.service.dto.BookDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookDetailController {
    private final BookDetailService bookDetailService;

    @GetMapping("/{bookId}")
    public String bookDetail(@PathVariable String bookId, Model model) {
        BookDetail bookDetail = bookDetailService.getBookDetailInfo(bookId);
        model.addAttribute("bookDetail", bookDetail);
        return "public-page/book-detail";
    }
}
