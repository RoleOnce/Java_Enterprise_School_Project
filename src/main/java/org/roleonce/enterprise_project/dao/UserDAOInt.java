package org.roleonce.enterprise_project.dao;

import org.roleonce.enterprise_project.model.CustomUser;

import java.util.List;
import java.util.Optional;

public interface UserDAOInt {

    Optional<CustomUser> findByUsername (String username);
    void save(CustomUser customUser);
    List<CustomUser> findAll();
    Optional<CustomUser> findById(Long id);
    void deleteById(Long id);

}
