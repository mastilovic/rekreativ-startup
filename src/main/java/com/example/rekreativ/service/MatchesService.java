package com.example.rekreativ.service;

import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Team;

public interface MatchesService {

    public void matchOutcome(Matches newMatch, Team existingTeamA, Team existingTeamB);

    public Matches createNewMatchup(String teamOne, String teamTwo, Integer teamOneScore, Integer teamTwoScore);

    public Matches save(Matches matches);

    public Iterable<Matches> findAll();

    public Matches findMatchById(Long id);

    public void delete(Long id);
}
