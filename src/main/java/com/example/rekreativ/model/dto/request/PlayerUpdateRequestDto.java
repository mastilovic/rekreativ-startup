package com.example.rekreativ.model.dto.request;

public class PlayerUpdateRequestDto {
    private Long id;
    private Long userId;
    private String playerType;
    private String fullName;

    public PlayerUpdateRequestDto() {
    }

    public PlayerUpdateRequestDto(Long id, Long userId, String playerType, String fullName) {
        this.id = id;
        this.userId = userId;
        this.playerType = playerType;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
