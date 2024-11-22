package com.bookshelf.bookproject.seller.repository.querydsl;

import com.bookshelf.bookproject.seller.repository.dto.AllCategoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookshelf.bookproject.domain.QCategory.category;
import static com.bookshelf.bookproject.domain.QSubSubcategory.subSubcategory;
import static com.bookshelf.bookproject.domain.QSubcategory.subcategory;

@RequiredArgsConstructor
public class CustomManagementServiceImpl implements CustomManagementService{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AllCategoryDto> findAllCategories() {
        return queryFactory
                .select(Projections.constructor(AllCategoryDto.class,
                                category.name,
                                subcategory.name,
                                subSubcategory.name))
                .from(subSubcategory)
                .join(subSubcategory.subcategory, subcategory)
                .join(subcategory.category, category)
                .fetch();
    }
}
