package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paths", uniqueConstraints =
        @UniqueConstraint(name = "unique_contexts", columnNames = "contexts")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Path {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contexts", nullable = false, insertable = false, updatable = false)
    private String context;

    public Path(String context) {
        this.context = context;
    }
}
