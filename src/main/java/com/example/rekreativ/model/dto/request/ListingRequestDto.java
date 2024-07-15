package com.example.rekreativ.model.dto.request;

import com.example.rekreativ.model.enums.ListingType;
import com.example.rekreativ.model.enums.PlayerType;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

public class ListingRequestDto {
    private ListingType listingType;
    private Set<PlayerType> lookingFor;
    private String title;
    private String description;
    private Instant matchDate;
    private Integer needPlayersCount;
    private Boolean isActive;
    private Long userId; //todo: get userId through pathVariable, not object property

    public ListingRequestDto() {
    }

    public ListingRequestDto(ListingType listingType,
                             Set<PlayerType> lookingFor,
                             String title,
                             String description,
                             Instant matchDate,
                             Integer needPlayersCount,
                             Boolean isActive,
                             Long userId) {
        this.listingType = listingType;
        this.lookingFor = lookingFor;
        this.title = title;
        this.description = description;
        this.matchDate = matchDate;
        this.needPlayersCount = needPlayersCount;
        this.isActive = isActive;
        this.userId = userId;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public void setListingType(ListingType listingType) {
        this.listingType = listingType;
    }

    public Set<PlayerType> getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(Set<PlayerType> lookingFor) {
        this.lookingFor = lookingFor;
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

    public Instant getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Instant matchDate) {
        this.matchDate = matchDate;
    }

    public Integer getNeedPlayersCount() {
        return needPlayersCount;
    }

    public void setNeedPlayersCount(Integer needPlayersCount) {
        this.needPlayersCount = needPlayersCount;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
