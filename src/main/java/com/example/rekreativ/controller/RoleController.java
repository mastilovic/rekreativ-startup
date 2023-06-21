package com.example.rekreativ.controller;

import com.example.rekreativ.model.Role;
import com.example.rekreativ.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    //save
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(path = "/save", method = RequestMethod.POST) //todo: change mappings
    public ResponseEntity<?> saveRole(@RequestBody Role role) {
        Role newRole = roleService.save(role);

        return new ResponseEntity<Object>(newRole, HttpStatus.CREATED);
    }

    //get all
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(){
        Iterable<Role> role = roleService.findAll();

        return new ResponseEntity<Object>(role, HttpStatus.OK);
    }
}
