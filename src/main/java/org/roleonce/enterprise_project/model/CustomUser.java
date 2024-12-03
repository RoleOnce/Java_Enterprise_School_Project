package org.roleonce.enterprise_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.roleonce.enterprise_project.authorities.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Entity
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Size(min = 4, max = 50)
    @Column(unique = true, nullable = false)
    private String username;
    @NotBlank
    @Size(min = 4, max = 80)
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is Required")
    private UserRole userRole;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public CustomUser() {}
    public CustomUser(String username, String password, UserRole userRole, boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    public CustomUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
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

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return userRole.getAuthorities();
    }

    public List<String> getListOfPermissions() {
        return userRole.getPermissions();
    }
}
