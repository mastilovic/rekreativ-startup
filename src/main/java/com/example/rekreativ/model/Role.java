package com.example.rekreativ.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name can't be empty!")
    private String name;

    @ManyToMany(mappedBy="roles")
    private List<User> user = new ArrayList<>();

    public Role(){
        super();
    }

    public Role(String name) {
        super();
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

