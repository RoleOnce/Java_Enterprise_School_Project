package org.roleonce.enterprise_project.service;

import org.roleonce.enterprise_project.model.CustomUser;
import org.roleonce.enterprise_project.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void deleteUser(String username) {
        // Hitta användaren och ta bort den från databasen
        CustomUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        userRepository.delete(user);
    }
}
