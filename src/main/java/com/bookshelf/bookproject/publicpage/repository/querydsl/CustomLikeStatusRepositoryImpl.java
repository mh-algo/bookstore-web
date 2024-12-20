package com.bookshelf.bookproject.publicpage.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

import static com.bookshelf.bookproject.domain.QLikeStatus.likeStatus;

@RequiredArgsConstructor
public class CustomLikeStatusRepositoryImpl implements CustomLikeStatusRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Set<Long> findReviewIdByBookProductId(Long bookProductId) {
        return queryFactory
                .select(likeStatus.review.id)
                .from(likeStatus)
                .where(likeStatus.review.bookProduct.id.eq(bookProductId))
                .fetch()
                .stream().collect(Collectors.toUnmodifiableSet());
    }
}
