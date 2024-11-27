package com.bookshelf.bookproject.seller.controller.dto.product.item;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class Product {
    @NotBlank
    private String name;

    @Max(1000000)
    @PositiveOrZero
    @NotNull
    private Integer price;

    @PositiveOrZero
    @NotNull
    private Integer discount;

    @Max(1000)
    @PositiveOrZero
    @NotNull
    private Integer amount;

    @Pattern(regexp = "^[0-9]+$")
    private String isbn;

    public int getDiscountPrice() {
        if (price != null && discount != null) {
            return price - discount;
        }
        return 0;
    }
}
