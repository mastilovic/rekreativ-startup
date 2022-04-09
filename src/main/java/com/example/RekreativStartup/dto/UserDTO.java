package com.example.RekreativStartup.dto;

import com.example.RekreativStartup.model.Role;

import java.util.ArrayList;
import java.util.Collection;

public class UserDTO {
    private Long id;
    private String username;
    private Collection<Role> roles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
