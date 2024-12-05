package org.roleonce.enterprise_project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MovieDTO {
    @NotNull(message = "Movie ID is required.")
    @Positive(message = "Movie ID must be greater than 0.")
    private Long movieId;

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    // Getters and setters
}
