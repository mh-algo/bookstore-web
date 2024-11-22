package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints =
        @UniqueConstraint(name = "unique_category_id_subcategory_name", columnNames = {"category_id, subcategory_name"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_subcategory_category"))
    private Category category;

    @Column(name = "subcategory_name", length = 20, nullable = false, insertable = false, updatable = false)
    private String name;
}