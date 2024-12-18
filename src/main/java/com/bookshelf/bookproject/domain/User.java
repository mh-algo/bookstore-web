package com.bookshelf.bookproject.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@DiscriminatorValue("U")
@PrimaryKeyJoinColumn(name = "id", foreignKey = @ForeignKey(name = "fk_users_accounts"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends Account {
    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String phone;

    @Builder
    public User(String name, String accountId, String password, AccountStatus status, String email, String phone) {
        super(name, accountId, password, status);
        this.email = email;
        this.phone = phone;
    }
}
