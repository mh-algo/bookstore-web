package com.bookshelf.bookproject.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints =
        @UniqueConstraint(name = "unique_email", columnNames = "email")
)
@DiscriminatorValue("USER")
@PrimaryKeyJoinColumn(name = "id", foreignKey = @ForeignKey(name = "fk_users_accounts"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;
    @Column(length = 20)
    private String phone;

    @Builder
    public User(String name, String accountId, String password, AccountStatus status, String email, String phone) {
        super(name, accountId, password, status);
        this.email = email;
        this.phone = phone;
    }
}
