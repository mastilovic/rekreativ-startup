package com.example.rekreativ.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Teammate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Name can't be empty!")
    private String name;

    @Column(name = "total_games_played")
//    @NotNull(message = "total games played cant be null")
    private Integer totalGamesPlayed;

    @Column(name = "wins")
//    @NotNull(message = "Wins cant be null")
    private Integer wins;

    @Column(name = "win_rate")
//    @NotNull(message = "Win rate cant be null")
    private Double winRate;
    // TODO
    // change personalScore to totalGamesPlayed
    // add wins for teammate

    @JsonIgnoreProperties(value = {"teammates"})
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "teammates")
    private Collection<Team> team = new ArrayList<>();

    public Teammate() {
        super();
    }

    public Teammate(Long id, String name, Integer totalGamesPlayed, Integer wins, Double winRate) {
        super();
        this.id = id;
        this.name = name;
        this.totalGamesPlayed = totalGamesPlayed;
        this.wins = wins;
        this.winRate = winRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(Integer totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public Collection<Team> getTeam() {
        return team;
    }

    public void setTeam(Collection<Team> team) {
        this.team = team;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }
}
