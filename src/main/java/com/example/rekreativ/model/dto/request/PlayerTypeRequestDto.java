package com.example.rekreativ.model.dto.request;

import com.example.rekreativ.model.enums.PlayerType;

import java.util.Set;

public class PlayerTypeRequestDto {
    private Set<PlayerType> playerTypes;

    public PlayerTypeRequestDto() {
    }

    public PlayerTypeRequestDto(Set<PlayerType> playerTypes) {
        this.playerTypes = playerTypes;
    }

    public Set<PlayerType> getPlayerTypes() {
        return playerTypes;
    }

    public void setPlayerTypes(Set<PlayerType> playerTypes) {
        this.playerTypes = playerTypes;
    }
}
