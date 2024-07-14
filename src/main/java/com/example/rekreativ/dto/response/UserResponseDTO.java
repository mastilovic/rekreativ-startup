package com.example.rekreativ.dto.response;

import com.example.rekreativ.dto.request.ReviewRequestDto;
import com.example.rekreativ.model.Listing;
import com.example.rekreativ.model.Role;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserResponseDTO {
    private Long id;
    private String username;
    private Collection<Role> roles = new ArrayList<>();
    private double averageReviewRating;
    private Integer reviewsCount;
    private List<ReviewRequestDto> reviews = new ArrayList<>();
    private List<ListingResponseDto> listings = new ArrayList<>();


    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id,
                           String username,
                           Collection<Role> roles,
                           double averageReviewRating,
                           Integer reviewsCount,
                           List<ReviewRequestDto> reviews,
                           List<ListingResponseDto> listings) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.averageReviewRating = averageReviewRating;
        this.reviewsCount = reviewsCount;
        this.reviews = reviews;
        this.listings = listings;
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

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public List<ListingResponseDto> getListings() {
        return listings;
    }

    public void setListings(List<ListingResponseDto> listings) {
        this.listings = listings;
    }
}
