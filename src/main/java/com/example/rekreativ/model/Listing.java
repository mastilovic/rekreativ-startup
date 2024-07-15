package com.example.rekreativ.model;

import com.example.rekreativ.model.enums.ListingType;
import com.example.rekreativ.model.enums.PlayerType;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(name = "idx_is_active", columnList = "isActive")
})
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ListingType listingType;
    @ElementCollection(targetClass = PlayerType.class)
    @Enumerated(EnumType.STRING)
    private Set<PlayerType> lookingFor;
    private String title;
    private String description;
    private Instant createdAt;
    private Instant matchDate;
    private Integer needPlayersCount;
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;
    @Column
    @ElementCollection
    private List<User> signed = new ArrayList<>();
    //TODO: e.g. create listing, add user as SHOOTER as player type
    // deactivate listing, change player type for user
    // check if user is updated within listing
    // if updated, fix it so that it doesnt update
    // for proper history tracking
    @Column
    @ElementCollection
    private List<User> accepted = new ArrayList<>();

    public Listing() {
    }

    public Listing(Long id,
                   ListingType listingType,
                   Set<PlayerType> lookingFor,
                   String title,
                   String description,
                   Instant createdAt,
                   Instant matchDate,
                   Integer needPlayersCount,
                   Boolean isActive,
                   User createdBy,
                   List<User> signed,
                   List<User> accepted) {
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<User> getSigned() {
        return signed;
    }

    public void setSigned(List<User> signed) {
        this.signed = signed;
    }

    public List<User> getAccepted() {
        return accepted;
    }

    public void setAccepted(List<User> accepted) {
        this.accepted = accepted;
    }
}
