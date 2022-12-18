package com.example.rekreativ.service;

import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.model.Teammate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TeamService {

    Iterable<Team> findAll();

    Team findTeamById(Long id);

    Team getByTeamname(String teamName);

    void delete(Long id);

    Team saveTeamWithUsername(Team team, HttpServletRequest request);

    Team save(Team team);

    List<Teammate> findTeammatesInTeam(String teamName);

    Team addTeammateToTeam(String teamname, String teammateName);

    Team getTeamScore(Team team);

    public void decreaseGamesPlayedByOne(Matches existingMatch);

    Team deleteTeammateFromTeam(String teamname, String teammate);
}
