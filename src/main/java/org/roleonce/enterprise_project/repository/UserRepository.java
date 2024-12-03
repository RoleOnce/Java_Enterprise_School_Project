package org.roleonce.enterprise_project.repository;

import org.roleonce.enterprise_project.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);
}
