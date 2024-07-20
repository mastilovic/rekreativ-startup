package com.example.rekreativ.model.dto.response;

import java.time.Instant;

public class ReviewResponseDto {
    private Long id;
    private String title;
    private String description;
    private Integer rating;
    private Instant reviewDate;
    private Long playerId;

    public ReviewResponseDto() {
    }

    public ReviewResponseDto(Long id,
                             String title,
                             String description,
                             Integer rating,
                             Instant reviewDate,
                             Long playerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.playerId = playerId;
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

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}
