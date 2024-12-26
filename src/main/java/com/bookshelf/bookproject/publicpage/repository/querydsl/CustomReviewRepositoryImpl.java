package com.bookshelf.bookproject.publicpage.repository.querydsl;

import com.bookshelf.bookproject.publicpage.repository.dto.ReviewListDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.bookshelf.bookproject.domain.QAccount.account;
import static com.bookshelf.bookproject.domain.QReview.review;

@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReviewListDto> findReviewListByBookId(Pageable pageable, Long bookProductId) {
        List<ReviewListDto> reviewList = createReviewListDto(
                queryFactory
                        .select(
                                review.id,
                                account.accountId,
                                review.rating,
                                review.context,
                                review.likeCount,
                                review.createdDate)
                        .from(review)
                        .join(review.account, account)
                        .where(review.bookProduct.id.eq(bookProductId))
                        .orderBy(review.likeCount.desc(), review.createdDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch()
        );

        JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(review)
                .where(review.bookProduct.id.eq(bookProductId));

        return PageableExecutionUtils.getPage(reviewList, pageable, countQuery::fetchOne);
    }

    private static List<ReviewListDto> createReviewListDto(List<Tuple> reviewList) {
        return reviewList.stream().map(tuple -> ReviewListDto.builder()
                .id(tuple.get(review.id))
                .accountId(tuple.get(account.accountId))
                .rating(tuple.get(review.rating))
                .context(tuple.get(review.context))
                .likeCount(tuple.get(review.likeCount))
                .createdDate(tuple.get(review.createdDate))
                .build()
        ).toList();
    }
}
