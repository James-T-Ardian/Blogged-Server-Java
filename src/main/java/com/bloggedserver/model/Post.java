package com.bloggedserver.model;

import java.util.OptionalInt;

public record Post(
        OptionalInt post_id,
        String title,
        String body,
        String created_at,
        String uploader
) {
}
