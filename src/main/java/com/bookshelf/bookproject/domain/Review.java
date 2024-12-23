package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Review extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accounts_id", nullable = false, foreignKey = @ForeignKey(name="fk_review_accounts"))
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_product_id", nullable = false, foreignKey = @ForeignKey(name="fk_review_book_product"))
    private BookProduct bookProduct;

    @Column(name = "contexts", length = 500, nullable = false)
    private String context;

    @Column(nullable = false, columnDefinition = "int default 0 constraint chk_rating check(rating between 0 and 5)")
    private Integer rating;

    @Column(name = "like_cnt", nullable = false, columnDefinition = "int default 0 constraint chk_like_cnt check(like_cnt >= 0)")
    private Integer likeCount;

    @Builder
    public Review(Account account, BookProduct bookProduct, String context, Integer rating, Integer likeCount) {
        this.account = account;
        this.bookProduct = bookProduct;
        this.context = context;
        this.rating = rating;
        this.likeCount = likeCount;
    }

    public static Review empty() {
        return Review.builder().build();
    }

    public boolean isEmpty() {
        return id == null;
    }
}
