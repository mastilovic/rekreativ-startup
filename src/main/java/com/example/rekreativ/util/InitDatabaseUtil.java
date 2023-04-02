package com.example.rekreativ.util;

import com.example.rekreativ.model.Role;
import com.example.rekreativ.model.User;
import com.example.rekreativ.service.RoleService;
import com.example.rekreativ.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class InitDatabaseUtil {

    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public InitDatabaseUtil(UserService userService,
                            RoleService roleService,
                            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initRoleAndUser() {
        log.debug("calling initRoleAndUser method");

        if (!roleService.existsByName("ROLE_ADMIN")) {
            Role adminRole = new Role("ROLE_ADMIN");
            roleService.initSave(adminRole);
        }

        if (!roleService.existsByName("ROLE_USER")) {
            Role userRole = new Role("ROLE_USER");
            roleService.initSave(userRole);
        }

        if (!userService.existsByUsername("admin")) {
            Role adminRole = roleService.findByName("ROLE_ADMIN");

            final String adminpass = "admin";
            User adminUser = new User(
                    null,
                    "admin",
                    passwordEncoder.encode(adminpass));
            adminUser.getRoles().add(adminRole);

            userService.initSave(adminUser);
        }
    }
}
