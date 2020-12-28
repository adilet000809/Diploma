package com.diploma.app.config;

import com.diploma.app.model.Roles;
import com.diploma.app.model.Users;
import com.diploma.app.repository.RoleRepository;
import com.diploma.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomCommandLineRunner.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public CustomCommandLineRunner(
            UserService userService,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String...args) throws Exception {
        if (!userService.existsByUserName("admin")) {
            Roles roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            List<Roles> adminRoles = new ArrayList<>();
            adminRoles.add(roleAdmin);
            Users admin = new Users();
            admin.setUserName("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(adminRoles);
            admin.setEmail("admin@mail.com");
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setPasswordLastChangedDate(new Date());
            userService.register(admin);
            LOGGER.trace("Admin did not exist in db and inserted successfully.");
        }
    }
}
