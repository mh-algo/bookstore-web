package com.bookshelf.bookproject.publics.controller;

import com.bookshelf.bookproject.common.CategoryService;
import com.bookshelf.bookproject.publics.service.BookListService;
import com.bookshelf.bookproject.publics.service.dto.BookListInfo;
import com.bookshelf.bookproject.seller.service.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookListController {
    private final CategoryService categoryService;
    private final BookListService bookListService;

    @ModelAttribute("categoryMap")
    private Map<Long, CategoryDto> addCategoryMap() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/books/list")
    public String list(Pageable pageable, Model model) {
        Page<BookListInfo> booksPage = bookListService.getBooksPage(pageable);
        model.addAttribute("booksPage", booksPage);
        return "publics/category-list";
    }
}
