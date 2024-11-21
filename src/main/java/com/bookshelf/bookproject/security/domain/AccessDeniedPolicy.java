package com.bookshelf.bookproject.security.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints =
        @UniqueConstraint(name = "unique_request", columnNames = "request_paths_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccessDeniedPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_paths_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_access_denied_policy_paths_request"))
    private Path requestPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "redirect_paths_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_access_denied_policy_paths_redirect"))
    private Path redirectPath;
}
