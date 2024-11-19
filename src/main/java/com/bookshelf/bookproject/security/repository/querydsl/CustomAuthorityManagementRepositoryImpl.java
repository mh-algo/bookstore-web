package com.bookshelf.bookproject.security.repository.querydsl;

import com.bookshelf.bookproject.security.repository.dto.AuthorityDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookshelf.bookproject.security.domain.QAuthorityManagement.authorityManagement;
import static com.bookshelf.bookproject.security.domain.QPath.path;
import static com.bookshelf.bookproject.domain.QRole.role;

@RequiredArgsConstructor
public class CustomAuthorityManagementRepositoryImpl implements CustomAuthorityManagementRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AuthorityDto> findAuthorityAll() {
        return queryFactory
                .select(Projections.constructor(AuthorityDto.class, role.type, path.context))
                .from(authorityManagement)
                .join(authorityManagement.role, role)
                .join(authorityManagement.path, path)
                .fetch();
    }
}
