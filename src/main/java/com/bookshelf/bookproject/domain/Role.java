package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles", uniqueConstraints =
        @UniqueConstraint(name = "unique_role_type", columnNames = "role_type")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_type", length = 20, nullable = false)
    private String type;
}
