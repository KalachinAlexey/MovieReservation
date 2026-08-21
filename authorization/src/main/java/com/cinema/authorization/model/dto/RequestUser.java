package com.cinema.authorization.model.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestUser(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
