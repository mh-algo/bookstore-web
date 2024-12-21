package com.bookshelf.bookproject.publicpage.repository.querydsl;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
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

    @Override
    public Boolean existsByReviewIdAndAccountId(Long reviewId, Long accountEntityId) {
        Integer count = queryFactory
                .select(Wildcard.countAsInt)
                .from(likeStatus)
                .where(likeStatus.review.id.eq(reviewId), likeStatus.account.id.eq(accountEntityId))
                .fetchOne();
        return Objects.requireNonNullElse(count, 0) == 1;
    }
}
