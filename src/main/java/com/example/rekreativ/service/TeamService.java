package com.example.rekreativ.service;

import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.model.Teammate;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.util.List;

public interface TeamService {

    public Iterable<Team> findAll();

    public Team findTeamById(Long id);

    public Team getByTeamname(String teamName);

    public void delete(Long id);

    public Team saveTeamWithUsername(Team team, HttpServletRequest request);

    public Team save(Team team);

    public List<Teammate> findTeammatesInTeam(String teamName);

    public Team addTeammateToTeam(String teamname, String teammateName) throws ValidationException;

    public Team getTeamScore(Team team);

    public void decreaseGamesPlayedByOne(Matches existingMatch);

    Team deleteTeammateFromTeam(String teamname, String teammate);
}
