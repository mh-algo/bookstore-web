package com.bookshelf.bookproject.common.repository.querydsl;

import com.bookshelf.bookproject.domain.SubSubcategory;
import com.bookshelf.bookproject.common.repository.dto.AllCategoryDto;

import java.util.List;

public interface CustomSubSubcategoryRepository {
    List<AllCategoryDto> findAllCategories();

    SubSubcategory findSubSubcategoryById(Long id);
}
