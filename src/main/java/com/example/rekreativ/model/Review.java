package com.example.rekreativ.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer rating;
    private Instant reviewDate;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public Review() {
    }

    public Review(Long id, String title, String description, Integer rating, Instant reviewDate, Player player) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Instant getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Instant reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
