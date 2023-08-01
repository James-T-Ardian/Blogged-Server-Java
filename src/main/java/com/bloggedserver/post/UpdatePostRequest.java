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
public class UpdatePostRequest {
    @NotNull
    private String title;
    @NotNull
    private String body;
}
