package com.diploma.app.repository;

import com.diploma.app.model.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    @EntityGraph(value = "user_with_roles")
    Users findByUserName(String userName);

    @EntityGraph(value = "user_only")
    Users findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
}
