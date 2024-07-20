package com.example.rekreativ.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    //FIXME: fix all mappings and properties regarding playerCharacteristics -> playerType
    @Embedded
    private PlayerCharacteristics playerCharacteristics;

    @OneToOne(mappedBy = "player")
    private User user;

    public Player() {
    }

    public Player(Long id,
                  List<Review> reviews,
                  PlayerCharacteristics playerCharacteristics,
                  User user) {
        this.id = id;
        this.reviews = reviews;
        this.playerCharacteristics = playerCharacteristics;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public PlayerCharacteristics getPlayerCharacteristics() {
        return playerCharacteristics;
    }

    public void setPlayerCharacteristics(PlayerCharacteristics playerCharacteristics) {
        this.playerCharacteristics = playerCharacteristics;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
