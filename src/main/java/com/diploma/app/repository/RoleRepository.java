package com.diploma.app.repository;

import com.diploma.app.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Roles findByName(String name);
}
