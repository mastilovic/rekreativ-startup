package com.example.rekreativ.service;

import com.example.rekreativ.model.Role;

public interface RoleService {

    Role findByName(String name);
    Role save(Role role);
    Role initSave(Role role);
    boolean existsByName(String name);
    Iterable<Role> findAll();
}
