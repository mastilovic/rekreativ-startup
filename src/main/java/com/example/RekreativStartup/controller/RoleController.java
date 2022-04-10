package com.example.RekreativStartup.controller;

import com.example.RekreativStartup.Service.RoleService;
import com.example.RekreativStartup.model.Role;
import com.example.RekreativStartup.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/role/v1")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //save
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveRole(@RequestBody Role role) {
        if (ValidatorUtil.roleValidator(role)){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        Role newRole = new Role();
        newRole.setName(role.getName());
        newRole = roleService.save(newRole);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

    //get all
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(){
        Iterable<Role> role = roleService.findAll();
        if(role == null) {

            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Object>(role, HttpStatus.OK);
    }
}
