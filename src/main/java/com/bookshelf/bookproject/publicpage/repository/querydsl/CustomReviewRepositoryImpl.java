package com.bookshelf.bookproject.publicpage.repository.querydsl;

import com.bookshelf.bookproject.publicpage.repository.dto.ReviewListDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookshelf.bookproject.domain.QAccount.account;
import static com.bookshelf.bookproject.domain.QReview.review;

@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReviewListDto> findReviewListByBookId(Long bookId) {
        return createReviewListDto(
                queryFactory
                        .select(
                                review.id,
                                account.accountId,
                                review.rating,
                                review.context,
                                review.createdDate)
                        .from(review)
                        .join(review.account, account)
                        .where(review.bookProduct.id.eq(bookId))
                        .orderBy(review.likeCount.desc(), review.createdDate.desc())
                        .fetch()
        );
    }

    private static List<ReviewListDto> createReviewListDto(List<Tuple> reviewList) {
        return reviewList.stream().map(tuple -> ReviewListDto.builder()
                .id(tuple.get(review.id))
                .accountId(tuple.get(account.accountId))
                .rating(tuple.get(review.rating))
                .context(tuple.get(review.context))
                .createdDate(tuple.get(review.createdDate))
                .build()
        ).toList();
    }
}
