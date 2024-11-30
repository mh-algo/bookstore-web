package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_product_id", nullable = false, foreignKey = @ForeignKey(name="fk_images_book_product"))
    private BookProduct bookProduct;

    @Column(name = "sub_image_url", nullable = false)
    private String subImageUrl;

    public Images(BookProduct bookProduct, String subImageUrl) {
        this.bookProduct = bookProduct;
        this.subImageUrl = subImageUrl;
    }
}
