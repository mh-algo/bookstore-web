package com.bookshelf.bookproject.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheConstants {
    public static final String CACHE_RESOLVER = "cacheResolver";

    public static final String ACCOUNT = "account";
    public static final String SELLER = "seller";
    public static final String BOOK_LIST = "bookList";
    public static final String REVIEW = "review";
}
