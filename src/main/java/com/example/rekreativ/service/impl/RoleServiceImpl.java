package com.example.rekreativ.service.impl;

import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Role;
import com.example.rekreativ.repository.RoleRepository;
import com.example.rekreativ.service.RoleService;
import com.example.rekreativ.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ValidatorUtil validatorUtil;

    public RoleServiceImpl(RoleRepository roleRepository,
                           ValidatorUtil validatorUtil) {

        this.roleRepository = roleRepository;
        this.validatorUtil = validatorUtil;
    }

    public Role findByName(String name) {
        log.debug("calling findByName method");

        return roleRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException(Role.class, name));
    }

    public Role save(Role role) {
        log.debug("calling save method");

        if (existsByName(role.getName())) {
            log.debug("Role already exists with name: {}", role.getName());

            throw new ObjectAlreadyExistsException(Role.class, role.getName());
        }

        Role newRole = new Role();
        newRole.setName(role.getName());
        validatorUtil.validate(role);

        return roleRepository.save(newRole);
    }

    public Role initSave(Role role) {

        return roleRepository.save(role);
    }

    @Override
    public boolean existsByName(String name) {

        return roleRepository.findByName(name).isPresent();
    }

    public Iterable<Role> findAll() {
        // todo: create check for empty list and return no content 204
        return roleRepository.findAll();
    }
}