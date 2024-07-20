package com.example.rekreativ.model.dto.response;

import com.example.rekreativ.model.Player;
import com.example.rekreativ.model.dto.UserListingDto;
import com.example.rekreativ.model.enums.ListingType;
import com.example.rekreativ.model.enums.PlayerType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListingResponseDto {
    private Long id;
    private ListingType listingType;
    private Set<PlayerType> lookingFor;
    private String title;
    private String description;
    private Instant createdAt;
    private Instant matchDate;
    private Integer needPlayersCount;
    private Boolean isActive;
    private UserListingDto createdBy;
    private List<Player> signed = new ArrayList<>();
    private List<Player> accepted = new ArrayList<>();

    public ListingResponseDto() {
    }

    public ListingResponseDto(Long id,
                              ListingType listingType,
                              Set<PlayerType> lookingFor,
                              String title,
                              String description,
                              Instant createdAt,
                              Instant matchDate,
                              Integer needPlayersCount,
                              Boolean isActive,
                              UserListingDto createdBy,
                              List<Player> signed,
                              List<Player> accepted) {
        this.id = id;
        this.listingType = listingType;
        this.lookingFor = lookingFor;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.matchDate = matchDate;
        this.needPlayersCount = needPlayersCount;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.signed = signed;
        this.accepted = accepted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
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

    public UserListingDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserListingDto createdBy) {
        this.createdBy = createdBy;
    }

    public List<Player> getSigned() {
        return signed;
    }

    public void setSigned(List<Player> signed) {
        this.signed = signed;
    }

    public List<Player> getAccepted() {
        return accepted;
    }

    public void setAccepted(List<Player> accepted) {
        this.accepted = accepted;
    }
}
