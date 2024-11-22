package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints =
        @UniqueConstraint(name = "unique_subcategory_id_sub_subcategory_name", columnNames = {"subcategory_id, sub_subcategory_name"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubSubcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_sub_subcategory_subcategory"))
    private Subcategory subcategory;

    @Column(name = "sub_subcategory_name", length = 20, nullable = false, insertable = false, updatable = false)
    private String name;
}
