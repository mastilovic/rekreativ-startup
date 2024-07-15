package com.example.rekreativ.model.dto.request;

import com.example.rekreativ.model.enums.PlayerType;

import java.util.Set;

public class UserListingUpdateRequestDto {
    private Long userId;
    private Long listingId;
    private Set<PlayerType> playerTypes;

    public UserListingUpdateRequestDto() {
    }

    public UserListingUpdateRequestDto(Long userId, Long listingId, Set<PlayerType> playerTypes) {
        this.userId = userId;
        this.listingId = listingId;
        this.playerTypes = playerTypes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public Set<PlayerType> getPlayerTypes() {
        return playerTypes;
    }

    public void setPlayerTypes(Set<PlayerType> playerTypes) {
        this.playerTypes = playerTypes;
    }
}
