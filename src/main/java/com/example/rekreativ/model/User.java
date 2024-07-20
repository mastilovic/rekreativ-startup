package com.example.rekreativ.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is mandatory!")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory!")
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new ArrayList<>();
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<Listing> listings = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Player player;

    private Boolean activeListing;

    public User() {
        super();
    }

    public User(Long id, String username, String password) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Username is mandatory!") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is mandatory!") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Password is mandatory!") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is mandatory!") String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public List<Listing> getListings() {
        return listings;
    }

    public void setListings(List<Listing> listings) {
        this.listings = listings;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Boolean getActiveListing() {
        return activeListing;
    }

    public void setActiveListing(Boolean activeListing) {
        this.activeListing = activeListing;
    }
}
