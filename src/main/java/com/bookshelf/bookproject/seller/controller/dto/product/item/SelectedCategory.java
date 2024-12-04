package com.bookshelf.bookproject.seller.controller.dto.product.item;

import com.bookshelf.bookproject.common.repository.dto.Category;
import lombok.Data;

@Data
public class SelectedCategory extends Category {
    private String categoryName;
    private String subcategoryName;
    private String subSubcategoryName;

    public String getCategoryPath() {
        return categoryName + " > " + subcategoryName + " > " + subSubcategoryName;
    }

    public boolean categoryExists() {
        return !categoryName.isEmpty() && !subcategoryName.isEmpty() && !subSubcategoryName.isEmpty();
    }

    public void setCategoriesName(String categoryName, String subcategoryName, String subSubcategoryName) {
        this.categoryName = categoryName;
        this.subcategoryName = subcategoryName;
        this.subSubcategoryName = subSubcategoryName;
    }
}
