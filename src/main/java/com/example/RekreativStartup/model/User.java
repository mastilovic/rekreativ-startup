package com.example.RekreativStartup.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private Integer personalScore;

    @OneToMany(mappedBy = "user", targetEntity = Team.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Team> team;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new ArrayList<>();
    
    public User (){
        super();
    }

    public User(Long id, String username, String password, Integer personalScore, Set<Team> team) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.personalScore = personalScore;
        this.team = team;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Set<Team> getTeam() {
        return team;
    }

    public void setTeam(Set<Team> team) {
        this.team = team;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Integer getPersonalScore() {
        return personalScore;
    }

    public void setPersonalScore(Integer personalScore) {
        this.personalScore = personalScore;
    }
}
