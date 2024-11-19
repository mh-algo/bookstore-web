package com.bookshelf.bookproject.security.repository.querydsl;


import com.bookshelf.bookproject.security.domain.QPath;
import com.bookshelf.bookproject.security.repository.dto.AccessDeniedPolicyDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookshelf.bookproject.security.domain.QAccessDeniedPolicy.accessDeniedPolicy;

@RequiredArgsConstructor
public class CustomAccessDeniedPolicyRepositoryImpl implements CustomAccessDeniedPolicyRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AccessDeniedPolicyDto> findAllAccessDeniedPolicy() {
        QPath requestPath = new QPath("requestPath");
        QPath redirectPath = new QPath("redirectPath");
        return queryFactory
                .select(Projections.constructor(AccessDeniedPolicyDto.class, requestPath.context, redirectPath.context))
                .from(accessDeniedPolicy)
                .join(accessDeniedPolicy.requestPath, requestPath)
                .join(accessDeniedPolicy.redirectPath, redirectPath)
                .fetch();
    }
}
