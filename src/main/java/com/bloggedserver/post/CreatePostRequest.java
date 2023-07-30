package com.bloggedserver.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Note: refer to constraints set in Post model to get an idea of the method parameter constraints in this class
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {
    @NotNull
    private String title;
    @NotNull
    private String body;
    @NotNull
    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])")
    private String created_at;
}
