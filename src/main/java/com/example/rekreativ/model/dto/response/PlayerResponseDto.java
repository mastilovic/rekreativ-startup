package com.example.rekreativ.model.dto.response;

import com.example.rekreativ.model.PlayerCharacteristics;
import com.example.rekreativ.model.Review;

import java.util.ArrayList;
import java.util.List;

public class PlayerResponseDto {
    private Long id;
    private List<ReviewResponseDto> reviews = new ArrayList<>();
    private PlayerCharacteristics playerCharacteristics;
    private Long userId;

    public PlayerResponseDto() {
    }

    public PlayerResponseDto(Long id,
                             List<ReviewResponseDto> reviews,
                             PlayerCharacteristics playerCharacteristics,
                             Long userId) {
        this.id = id;
        this.reviews = reviews;
        this.playerCharacteristics = playerCharacteristics;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ReviewResponseDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewResponseDto> reviews) {
        this.reviews = reviews;
    }

    public PlayerCharacteristics getPlayerCharacteristics() {
        return playerCharacteristics;
    }

    public void setPlayerCharacteristics(PlayerCharacteristics playerCharacteristics) {
        this.playerCharacteristics = playerCharacteristics;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
