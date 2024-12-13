package com.bookshelf.bookproject.publicpage.controller;

import com.bookshelf.bookproject.common.CategoryService;
import com.bookshelf.bookproject.publicpage.service.BookListService;
import com.bookshelf.bookproject.publicpage.service.dto.BookList;
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

import static com.bookshelf.bookproject.publicpage.BookServiceUtil.validateStringId;

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
    public String list(@RequestParam(value = "category", required = false) String categoryId, Pageable pageable, Model model) {
        if (validateStringId(categoryId)) {
            return addBooks(model, pageable, categoryId);
        }
        return "redirect:/books/list";
    }

    private String addBooks(Model model, Pageable pageable, String category) {
        Page<BookList> booksPage = category == null ?
                bookListService.getBookListPage(pageable) : bookListService.getBookListPageByCategory(pageable, category);
        model.addAttribute("booksPage", booksPage);
        return "public-page/book-list";
    }
}
