package com.example.RekreativStartup.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String teamName;

//    @ManyToMany(mappedBy="team")
//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user; //= new ArrayList<>();

//    @ManyToOne(optional = true, fetch = FetchType.LAZY)
//    @JoinTable(name = "team_team",
//            joinColumns = { @JoinColumn(name = "parent_team_id", referencedColumnName = "id", insertable = false, updatable = false) },
//            inverseJoinColumns = { @JoinColumn(name = "child_team_id", referencedColumnName = "id", insertable = false, updatable = false) } )
//    private Team parentTeam;
//
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "team_team",
//            joinColumns = {
//                    @JoinColumn(name = "parent_team_id",
//                            referencedColumnName = "id",
//                            insertable = false,
//                            updatable = false)
//            },
//            inverseJoinColumns = {
//                    @JoinColumn(name = "child_team_id",
//                            referencedColumnName = "id",
//                            insertable = false,
//                            updatable = false) } )
//    private List<Team> childTeam;

    @ElementCollection
    private Collection<String> teammate= new ArrayList<String>();

    private String city;

    private Integer score;

    public Team(){
        super();
    }

    public Team(Long id, String teamName, String city, Integer score) {
        super();
        this.id = id;
        this.teamName = teamName;
        this.city = city;
        this.score = score;
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


    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Collection<String> getTeammate() {
        return teammate;
    }

    public void setTeammate(Collection<String> teammate) {
        this.teammate = teammate;
    }
}
