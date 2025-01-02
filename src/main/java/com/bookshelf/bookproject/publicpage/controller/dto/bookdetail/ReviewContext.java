package com.bookshelf.bookproject.publicpage.controller.dto.bookdetail;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewContext {
    @NotNull
    @Size(min = 1, max = 500)
    private String context;

    public static ReviewContext of(String context) {
        return new ReviewContext(context);
    }
}
