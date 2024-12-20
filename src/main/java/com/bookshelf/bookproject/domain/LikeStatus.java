package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like_status", uniqueConstraints =
        @UniqueConstraint(name = "unique_review_id_accounts_id", columnNames = {"review_id", "accounts_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LikeStatus extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false,  foreignKey = @ForeignKey(name="fk_like_status_review"))
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accounts_id", nullable = false,  foreignKey = @ForeignKey(name="fk_like_status_accounts"))
    private Account account;
}
