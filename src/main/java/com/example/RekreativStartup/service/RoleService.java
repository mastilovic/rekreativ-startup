package com.example.RekreativStartup.service;

import com.example.RekreativStartup.model.Role;
import com.example.RekreativStartup.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Iterable<Role> findAll(){
        return roleRepository.findAll();
    }
}