package com.diploma.app.service;

import com.diploma.app.model.Users;

import java.util.List;

public interface UserService {

    Users register(Users user);
    Users update(Users user);

    List<Users> getAll();

    Users findByUserName(String userName);
    Users findByEmail(String email);

    Users findById(Long id);

    void delete(Long id);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
}
