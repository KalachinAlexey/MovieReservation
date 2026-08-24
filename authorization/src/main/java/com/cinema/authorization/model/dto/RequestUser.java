package com.cinema.authorization.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestUser(
        @NotBlank
        @Size(min = 3, max = 100)
        String username,
        @NotBlank
        @Size(min = 8, max = 100)
        String password
) {
}
