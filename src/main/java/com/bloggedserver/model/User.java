package com.bloggedserver.model;

import jakarta.validation.constraints.NotNull;

public record User(@NotNull(message = "User's username cannot be null") String username,
                   @NotNull(message = "User's password cannot be null") String password) {
}
