package org.roleonce.enterprise_project.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.roleonce.enterprise_project.authorities.UserRole;

public class UserDTO {

    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 4, max = 80, message = "Password must be between 4 and 80 characters")
    private String password;

    private UserRole userRole;

    // Constructors
    public UserDTO() {}

    public UserDTO(CustomUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.userRole = user.getUserRole();
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    // Conversion method to convert DTO to Entity
    public CustomUser toEntity() {
        CustomUser user = new CustomUser(this.username, this.password);
        user.setUserRole(this.userRole);
        // Set default account states
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        return user;
    }
}
