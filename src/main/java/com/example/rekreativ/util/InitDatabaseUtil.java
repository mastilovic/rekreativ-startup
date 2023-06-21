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

    private static final String ROLE_ADMIN_NAME = "ROLE_ADMIN";
    private static final String ROLE_USER_NAME = "ROLE_USER";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

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

        if (!roleService.existsByName(ROLE_ADMIN_NAME)) {
            Role adminRole = new Role(ROLE_ADMIN_NAME);
            roleService.initSave(adminRole);
        }

        if (!roleService.existsByName(ROLE_USER_NAME)) {
            Role userRole = new Role(ROLE_USER_NAME);
            roleService.initSave(userRole);
        }

        if (!userService.existsByUsername(ADMIN_USERNAME)) {
            Role adminRole = roleService.findByName(ROLE_ADMIN_NAME);

            User adminUser = new User(
                    null,
                    ADMIN_USERNAME,
                    passwordEncoder.encode(ADMIN_PASSWORD));
            adminUser.getRoles().add(adminRole);

            userService.initSave(adminUser);
        }
    }
}
