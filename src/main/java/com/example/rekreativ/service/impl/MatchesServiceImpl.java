package com.example.rekreativ.service.impl;

import com.example.rekreativ.commons.CustomValidator;
import com.example.rekreativ.dto.request.MatchesRequestDTO;
import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.repository.MatchesRepository;
import com.example.rekreativ.service.MatchesService;
import com.example.rekreativ.service.TeamService;
import com.example.rekreativ.service.TeammateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MatchesServiceImpl implements MatchesService {

    private final MatchesRepository matchesRepository;
    private final TeammateService teammateService;
    private final TeamService teamService;
    private final CustomValidator customValidator;

    public MatchesServiceImpl(MatchesRepository matchesRepository,
                              TeammateServiceImpl teammateServiceImpl,
                              TeamServiceImpl teamService,
                              CustomValidator customValidator) {
        this.matchesRepository = matchesRepository;
        this.teammateService = teammateServiceImpl;
        this.teamService = teamService;
        this.customValidator = customValidator;
    }

    public Matches createMatchup(String teamOne, String teamTwo, Integer teamOneScore, Integer teamTwoScore) {
        log.debug("calling createMatchup method in MatchesServiceImpl");

        Team teamA = teamService.getByTeamname(teamOne);
        Team teamB = teamService.getByTeamname(teamTwo);

        Matches match = new Matches();
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setTeamAScore(teamOneScore);
        match.setTeamBScore(teamTwoScore);

        customValidator.validate(match);

        return matchesRepository.save(match);
    }

    public Matches save(MatchesRequestDTO matchDTO) {
        Team teamA = teamService.getByTeamname(matchDTO.getTeamOne());
        Team teamB = teamService.getByTeamname(matchDTO.getTeamTwo());

        boolean teammateInBothTeams = teamA.getTeammates().stream()
                .anyMatch(teamB.getTeammates()::contains);

        if (teammateInBothTeams) {
            log.debug("Teammate can't be part of both teams in a single match!");
            throw new ObjectAlreadyExistsException(Teammate.class, "Teammate can't be part of both teams in a single match");
        }

        Matches match = new Matches();
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setTeamAScore(matchDTO.getTeamOneScore());
        match.setTeamBScore(matchDTO.getTeamTwoScore());

        processMatchOutcome(match, teamA, teamB);

        return matchesRepository.save(match);
    }

    public void processMatchOutcome(Matches match, Team teamA, Team teamB) {
        log.debug("calling method processMatchOutcome in MatchesServiceImpl");

        if (match.getTeamAScore() > match.getTeamBScore()) {
            match.setWinner(match.getTeamA().getTeamName());
            updateScores(teamA, teamB, false);

        } else if (match.getTeamAScore() < match.getTeamBScore()) {
            match.setWinner(match.getTeamB().getTeamName());
            updateScores(teamB, teamA, false);

        } else if (match.getTeamAScore().equals(match.getTeamBScore())) {
            match.setWinner("draw");
            updateScores(teamA, teamB, true);
        }
    }

    private void updateScores(Team winner, Team loser, boolean isDraw) {
        log.debug("calling method updateScores in " + this.getClass().getSimpleName());

        winner.setWins(winner.getWins() + 1);
        increaseTeamTotalGamesPlayed(winner);
        increaseTeamTotalGamesPlayed(loser);

        if (isDraw) {
            //todo: can create different method
            // updateDrawScores - this method will take both teams
            // update them appropriately
            // implementation of this method would not call updateLosingTeammates
            updateLosingTeammates(winner);
            updateLosingTeammates(loser);
        } else {
            updateLosingTeammates(loser);
            updateWinningTeammates(winner);
        }

        teamService.save(winner);
        teamService.save(loser);
    }

    public Iterable<Matches> findAll() {
        return matchesRepository.findAll();
    }

    public Matches findMatchById(Long id) {
        log.debug("calling findMatchById method in MatchesServiceImpl");

        return matchesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Matches.class, id));
    }

    public void delete(Long id) {
        Matches existingMatch = findMatchById(id);
        teamService.decreaseGamesPlayedByOne(existingMatch);

        existingMatch.setTeamA(null);
        existingMatch.setTeamB(null);

        matchesRepository.deleteById(id);
    }

    private void updateWinningTeammates(Team team) {

        team.getTeammates().forEach(teammate -> {
            teammate.setWins(teammate.getWins() + 1);
            teammate.setTotalGamesPlayed(teammate.getTotalGamesPlayed() + 1);

            double winsDecimal = teammate.getWins();
            double winRate = (winsDecimal / teammate.getTotalGamesPlayed()) * 100;

            teammate.setWinRate(Precision.round(winRate, 2));

            teammateService.initSave(teammate);
        });
    }

    private void updateLosingTeammates(Team team) {

        team.getTeammates().forEach(teammate -> {
            teammate.setTotalGamesPlayed(teammate.getTotalGamesPlayed() + 1);

            double winsDecimal = teammate.getWins();
            double winRate = (winsDecimal / teammate.getTotalGamesPlayed()) * 100;

            teammate.setWinRate(Precision.round(winRate, 2));

            teammateService.initSave(teammate);
        });
    }

    private void increaseTeamTotalGamesPlayed(Team team) {
        team.setTotalGamesPlayed(team.getTotalGamesPlayed() + 1);
    }

}
