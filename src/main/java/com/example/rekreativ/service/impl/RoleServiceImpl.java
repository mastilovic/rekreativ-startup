package com.example.rekreativ.service.impl;

import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Role;
import com.example.rekreativ.repository.RoleRepository;
import com.example.rekreativ.service.RoleService;
import com.example.rekreativ.util.ValidatorUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ValidatorUtil validatorUtil;

    public RoleServiceImpl(RoleRepository roleRepository,
                           ValidatorUtil validatorUtil) {

        this.roleRepository = roleRepository;
        this.validatorUtil = validatorUtil;
    }

    public Role findByName(String name){
        Optional<Role> optionalRole = roleRepository.findByName(name);

        if(optionalRole.isEmpty()){

            throw new ObjectNotFoundException(Role.class, name);
        }

        return optionalRole.get();
    }

    public Role save(Role role) {

        findByName(role.getName());

        Role newRole = new Role();
        newRole.setName(role.getName());
        validatorUtil.roleValidator(role);

        return roleRepository.save(newRole);
    }

    public Role initSave(Role role) {

        return roleRepository.save(role);
    }

    @Override
    public boolean existsByName(String name) {

        return roleRepository.findByName(name).isPresent();
    }

    public Iterable<Role> findAll(){
        // todo: create check for empty list and return no content 204
        return roleRepository.findAll();
    }
}