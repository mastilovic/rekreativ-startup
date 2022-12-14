package com.example.rekreativ.service;

import com.example.rekreativ.model.Role;

public interface RoleService {

    public Role findByName(String name);
    public Role save(Role role);
    public Role initSave(Role role);
    public boolean existsByName(String name);
    public Iterable<Role> findAll();
}
