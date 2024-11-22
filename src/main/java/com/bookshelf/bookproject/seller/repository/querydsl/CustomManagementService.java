package com.bookshelf.bookproject.seller.repository.querydsl;

import com.bookshelf.bookproject.seller.repository.dto.AllCategoryDto;

import java.util.List;

public interface CustomManagementService {
    List<AllCategoryDto> findAllCategories();
}
