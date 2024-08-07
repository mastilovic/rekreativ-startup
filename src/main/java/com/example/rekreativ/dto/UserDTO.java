package com.example.rekreativ.dto;

import com.example.rekreativ.model.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private Collection<Role> roles = new ArrayList<>();

    private double averageReviewRating;

    private List<ReviewRequestDto> reviews = new ArrayList<>();

    public UserDTO() {
    }

    public UserDTO(Long id, String username, Collection<Role> roles, double averageReviewRating, List<ReviewRequestDto> reviews) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.averageReviewRating = averageReviewRating;
        this.reviews = reviews;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public List<ReviewRequestDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewRequestDto> reviews) {
        this.reviews = reviews;
    }

    public double getAverageReviewRating() {
        return averageReviewRating;
    }

    public void setAverageReviewRating(double averageReviewRating) {
        this.averageReviewRating = averageReviewRating;
    }
}
