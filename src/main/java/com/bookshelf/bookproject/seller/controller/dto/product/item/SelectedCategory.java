package com.bookshelf.bookproject.seller.controller.dto.product.item;

import lombok.Data;

@Data
public class SelectedCategory {
    private String categoryName;
    private String subcategoryName;
    private String subSubcategoryName;

    public String getCategoryPath() {
        return categoryName + " > " + subcategoryName + " > " + subSubcategoryName;
    }

    public boolean categoryExists() {
        return !categoryName.isEmpty() && !subcategoryName.isEmpty() && !subSubcategoryName.isEmpty();
    }
}
