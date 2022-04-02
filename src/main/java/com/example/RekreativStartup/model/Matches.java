package com.example.RekreativStartup.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Matches {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamA")
    private Team teamA;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamB")
    private Team teamB;

    private Integer teamAScore;

    private Integer teamBScore;

    public Matches(){
        super();
    }

    public Matches(Long id, Team teamA, Team teamB, Integer teamAScore, Integer teamBScore) {
        this.id = id;
        this.teamA = teamA;
        this.teamB = teamB;
        this.teamAScore = teamAScore;
        this.teamBScore = teamBScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public Integer getTeamAScore() {
        return teamAScore;
    }

    public void setTeamAScore(Integer teamAScore) {
        this.teamAScore = teamAScore;
    }

    public Integer getTeamBScore() {
        return teamBScore;
    }

    public void setTeamBScore(Integer teamBScore) {
        this.teamBScore = teamBScore;
    }
}
