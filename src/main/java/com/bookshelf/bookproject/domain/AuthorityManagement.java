package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints =
        @UniqueConstraint(name = "unique_roles_id_paths_id", columnNames = {"roles_id", "paths_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AuthorityManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roles_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_authority_management_roles"))
    private Role role;

    @ManyToOne
    @JoinColumn(name = "paths_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_authority_management_paths"))
    private Path path;
}
