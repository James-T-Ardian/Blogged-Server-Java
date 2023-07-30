package com.bloggedserver.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Optional;
import java.util.OptionalInt;

public record Post(OptionalInt post_id, @NotNull(message = "Post's title cannot be null or empty") String title,
                   @NotNull(message = "Post's body cannot be null or empty") String body,
                   @NotNull @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])") Optional<String> created_at,
                   @NotNull(message = "Post's uploader cannot be null or empty") String uploader) {
}
