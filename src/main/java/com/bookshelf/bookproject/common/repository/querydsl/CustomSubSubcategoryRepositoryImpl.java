package com.bookshelf.bookproject.common.repository.querydsl;

import com.bookshelf.bookproject.domain.SubSubcategory;
import com.bookshelf.bookproject.common.repository.dto.AllCategoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookshelf.bookproject.domain.QCategory.category;
import static com.bookshelf.bookproject.domain.QSubSubcategory.subSubcategory;
import static com.bookshelf.bookproject.domain.QSubcategory.subcategory;

@RequiredArgsConstructor
public class CustomSubSubcategoryRepositoryImpl implements CustomSubSubcategoryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AllCategoryDto> findAllCategories() {
        return queryFactory
                .select(Projections.constructor(AllCategoryDto.class,
                                category.id,
                                category.name,
                                subcategory.id,
                                subcategory.name,
                                subSubcategory.id,
                                subSubcategory.name))
                .from(subSubcategory)
                .join(subSubcategory.subcategory, subcategory)
                .join(subcategory.category, category)
                .fetch();
    }

    @Override
    public SubSubcategory findSubSubcategoryById(Long id) {
        return queryFactory
                .selectFrom(subSubcategory)
                .where(subSubcategory.id.eq(id))
                .fetchOne();
    }
}
