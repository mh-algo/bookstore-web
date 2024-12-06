package com.bookshelf.bookproject.publics.controller;

import com.bookshelf.bookproject.common.CategoryService;
import com.bookshelf.bookproject.publics.service.BookListService;
import com.bookshelf.bookproject.publics.service.dto.BookListInfo;
import com.bookshelf.bookproject.seller.service.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
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
    public String list(Model model) {
        List<BookListInfo> BookListInfo = bookListService.getAllBooksList();
        model.addAttribute("bookList", BookListInfo);

        return "publics/category-list";
    }
}
