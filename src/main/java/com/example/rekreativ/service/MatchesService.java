package com.example.rekreativ.service;

import com.example.rekreativ.dto.request.MatchesRequestDTO;
import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Team;

public interface MatchesService {

    void processMatchOutcome(Matches newMatch, Team existingTeamA, Team existingTeamB);

    Matches createMatchup(String teamOne, String teamTwo, Integer teamOneScore, Integer teamTwoScore);

    Matches save(MatchesRequestDTO matches);

    Iterable<Matches> findAll();

    Matches findMatchById(Long id);

    void delete(Long id);
}
