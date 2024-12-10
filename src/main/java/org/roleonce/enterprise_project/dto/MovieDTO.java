package org.roleonce.enterprise_project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovieDTO(
        @NotNull(message = "Movie ID is required.")
        @Positive(message = "Movie ID must be greater than 0.")
        Long movieId
) {
}
