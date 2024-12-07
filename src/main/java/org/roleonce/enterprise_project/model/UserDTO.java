package org.roleonce.enterprise_project.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.roleonce.enterprise_project.authorities.UserRole;

public record UserDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 7, max = 80, message = "Password must be between 7 and 80 characters")
        String password,

        UserRole userRole
) {
}
