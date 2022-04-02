package com.example.RekreativStartup.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
public class Teammate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private Integer personalScore;


//    private Set<Team> team;
//    @ManyToMany//(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    @JsonIgnoreProperties(value = {"teammates"})
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "teammates")
    private Collection<Team> team = new ArrayList<>();

    public Teammate() {
        super();
    }

    public Teammate(Long id, String name, Integer personalScore) {
        super();
        this.id = id;
        this.name = name;
        this.personalScore = personalScore;
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

    public Integer getPersonalScore() {
        return personalScore;
    }

    public void setPersonalScore(Integer personalScore) {
        this.personalScore = personalScore;
    }

    public Collection<Team> getTeam() {
        return team;
    }

    public void setTeam(Collection<Team> team) {
        this.team = team;
    }
}
