package com.example.rekreativ.model;

import javax.persistence.Embeddable;

@Embeddable
public class PlayerCharacteristics {
    private String playerType;
    private String fullName;

    public PlayerCharacteristics() {
    }

    public PlayerCharacteristics(String playerType, String fullName) {
        this.playerType = playerType;
        this.fullName = fullName;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
