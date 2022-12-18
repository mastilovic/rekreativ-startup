package com.example.rekreativ.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Team name can't be empty!")
    private String teamName;

    @JsonIgnoreProperties(value = {"team"})
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "team_teammates",
        joinColumns = { @JoinColumn(name = "team_id")},
        inverseJoinColumns = { @JoinColumn (name = "teammate_id")})
    private Collection<Teammate> teammates = new ArrayList<>();

    @NotBlank(message = "City can't be empty!")
    private String city;

    @NotNull
    private Integer wins;

    @NotNull
    private Integer totalGamesPlayed;

    public Team(){
        super();
    }

    public Team(Long id, String teamName, String city, Integer wins, Integer totalGamesPlayed) {
        super();
        this.id = id;
        this.teamName = teamName;
        this.city = city;
        this.wins = wins;
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Collection<Teammate> getTeammates() {
        return teammates;
    }

    public void setTeammates(Collection<Teammate> teammates) {
        this.teammates = teammates;
    }

    public Integer getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(Integer totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }
}


