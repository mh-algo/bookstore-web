package com.bookshelf.bookproject.publicpage.controller;

import com.bookshelf.bookproject.common.CategoryService;
import com.bookshelf.bookproject.publicpage.service.BookListService;
import com.bookshelf.bookproject.publicpage.service.dto.BookListInfo;
import com.bookshelf.bookproject.seller.service.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static com.bookshelf.bookproject.publicpage.service.BookListService.validateCategoryParam;

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
    public String list(@RequestParam(required = false) String category, Pageable pageable, Model model) {
        if (validateCategoryParam(category)) {
            return addBooks(model, pageable, category);
        }
        return "redirect:/books/list";
    }

    private String addBooks(Model model, Pageable pageable, String category) {
        Page<BookListInfo> booksPage = category == null ?
                bookListService.getAllBooks(pageable) : bookListService.getBooksByCategory(pageable, category);
        model.addAttribute("booksPage", booksPage);
        return "public-page/category-list";
    }
}
