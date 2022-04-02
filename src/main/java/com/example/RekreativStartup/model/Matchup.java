//package com.example.RekreativStartup.model;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//@Entity
//public class Matchup {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String opponentName;
//
//
//    @ManyToMany(mappedBy="matchups")
//    private Collection<Team> team = new ArrayList<>();
//
//    private Integer opponentScore;
//
//    public Matchup(){
//        super();
//    }
//
//    public Matchup(Long id, String opponentName, Collection<Team> team, Integer opponentScore) {
//        this.id = id;
//        this.opponentName = opponentName;
//        this.team = team;
//        this.opponentScore = opponentScore;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Collection<Team> getTeam() {
//        return team;
//    }
//
//    public void setTeam(Collection<Team> team) {
//        this.team = team;
//    }
//
//    public Integer getOpponentScore() {
//        return opponentScore;
//    }
//
//    public void setOpponentScore(Integer opponentScore) {
//        this.opponentScore = opponentScore;
//    }
//
//    public String getOpponentName() {
//        return opponentName;
//    }
//
//    public void setOpponentName(String opponentName) {
//        this.opponentName = opponentName;
//    }
//}
