package com.diploma.app.repository;

import com.diploma.app.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUserName(String userName);
    Users findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
}
