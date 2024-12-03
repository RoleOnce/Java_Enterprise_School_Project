package org.roleonce.enterprise_project.service;

import org.roleonce.enterprise_project.model.CustomUser;
import org.roleonce.enterprise_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Hämta användare för att kunna logga in
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        System.out.println("Authorities for user: " + user.getAuthorities());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                user.getAuthorities()
        );

    }
}
