package com.bookshelf.bookproject.seller.repository.querydsl;

import com.bookshelf.bookproject.domain.SubSubcategory;
import com.bookshelf.bookproject.seller.repository.dto.AllCategoryDto;

import java.util.List;

public interface CustomSubSubcategoryRepository {
    List<AllCategoryDto> findAllCategories();
    SubSubcategory findCategoryGroupByName(String name, String subName, String subSubName);
}
