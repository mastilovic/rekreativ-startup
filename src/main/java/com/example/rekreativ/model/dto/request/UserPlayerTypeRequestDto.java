package com.example.rekreativ.model.dto.request;

import com.example.rekreativ.model.enums.PlayerType;

import java.util.Set;

public class UserPlayerTypeRequestDto {
    private Long userId;
    private Set<PlayerType> playerTypes;

    public UserPlayerTypeRequestDto() {
    }

    public UserPlayerTypeRequestDto(Long userId, Set<PlayerType> playerTypes) {
        this.userId = userId;
        this.playerTypes = playerTypes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<PlayerType> getPlayerType() {
        return playerTypes;
    }

    public void setPlayerType(Set<PlayerType> playerTypes) {
        this.playerTypes = playerTypes;
    }
}
