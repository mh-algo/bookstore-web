package com.bookshelf.bookproject.seller.controller.dto.product;

import com.bookshelf.bookproject.seller.controller.dto.product.item.Image;
import com.bookshelf.bookproject.seller.controller.dto.product.item.SelectedCategory;
import lombok.Data;

import java.util.List;

@Data
public class RegisterInfo {
    private SelectedCategory selectedCategory;
    private String name;
    private Integer price;
    private Integer discount;
    private Integer amount;
    private Image image;
    private List<Image> images;
    private String imagesMessage;


    public int getDiscountPrice() {
        if (price != null && discount != null) {
            return price - discount;
        }
        return 0;
    }
}
