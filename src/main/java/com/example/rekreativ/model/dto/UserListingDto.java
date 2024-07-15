package com.example.rekreativ.model.dto;

import com.example.rekreativ.model.dto.request.ReviewRequestDto;
import com.example.rekreativ.model.Role;
import com.example.rekreativ.model.enums.PlayerType;

import java.util.*;

public class UserListingDto {
    private Long id;
    private String username;
    private Collection<Role> roles = new ArrayList<>();
    private double averageReviewRating;
    private Integer reviewsCount;
    private Set<PlayerType> playerTypes = new HashSet<>();
    private List<ReviewRequestDto> reviews = new ArrayList<>();

    public UserListingDto() {
    }

    public UserListingDto(Long id,
                          String username,
                          Collection<Role> roles,
                          double averageReviewRating,
                          Integer reviewsCount,
                          Set<PlayerType> playerTypes,
                          List<ReviewRequestDto> reviews) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.averageReviewRating = averageReviewRating;
        this.reviewsCount = reviewsCount;
        this.playerTypes = playerTypes;
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

    public double getAverageReviewRating() {
        return averageReviewRating;
    }

    public void setAverageReviewRating(double averageReviewRating) {
        this.averageReviewRating = averageReviewRating;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public List<ReviewRequestDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewRequestDto> reviews) {
        this.reviews = reviews;
    }

    public Set<PlayerType> getPlayerTypes() {
        return playerTypes;
    }

    public void setPlayerTypes(Set<PlayerType> playerTypes) {
        this.playerTypes = playerTypes;
    }
}
