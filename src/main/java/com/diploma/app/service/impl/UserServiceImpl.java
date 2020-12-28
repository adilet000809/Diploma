package com.diploma.app.service.impl;

import com.diploma.app.model.Roles;
import com.diploma.app.model.Users;
import com.diploma.app.repository.RoleRepository;
import com.diploma.app.repository.UserRepository;
import com.diploma.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Users register(Users user) {
        Roles roleUser = roleRepository.findByName("ROLE_USER");
        List<Roles> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setRoles(userRoles);

        return userRepository.save(user);
    }

    @Override
    public Users update(Users user) {
        return userRepository.save(user);

    }

    @Override
    public List<Users> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Users findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }
}

