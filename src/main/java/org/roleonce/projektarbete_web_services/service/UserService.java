package org.roleonce.projektarbete_web_services.service;

import org.roleonce.projektarbete_web_services.model.CustomUser;
import org.roleonce.projektarbete_web_services.repository.UserRepository;
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
