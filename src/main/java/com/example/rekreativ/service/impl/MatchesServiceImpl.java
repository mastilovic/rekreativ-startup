package com.example.rekreativ.service.impl;

import com.example.rekreativ.dto.MatchesRequestDTO;
import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.repository.MatchesRepository;
import com.example.rekreativ.service.MatchesService;
import com.example.rekreativ.service.TeamService;
import com.example.rekreativ.service.TeammateService;
import com.example.rekreativ.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Optional;


@Service
@Slf4j
public class MatchesServiceImpl implements MatchesService {

    private final MatchesRepository matchesRepository;
    private final TeammateService teammateService;
    private final TeamService teamService;
    private final ValidatorUtil validatorUtil;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Autowired
    public MatchesServiceImpl(MatchesRepository matchesRepository,
                              TeammateServiceImpl teammateServiceImpl,
                              TeamServiceImpl teamService,
                              ValidatorUtil validatorUtil) {
        this.matchesRepository = matchesRepository;
        this.teammateService = teammateServiceImpl;
        this.teamService = teamService;
        this.validatorUtil = validatorUtil;
    }


    public Matches createMatchup(String teamOne, String teamTwo, Integer teamOneScore, Integer teamTwoScore){
        log.debug("calling createMatchup method in MatchesServiceImpl");

        Team teamA = teamService.getByTeamname(teamOne);
        Team teamB = teamService.getByTeamname(teamTwo);

        Matches match = new Matches();
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setTeamAScore(teamOneScore);
        match.setTeamBScore(teamTwoScore);

        validatorUtil.validate(match);

        return matchesRepository.save(match);
    }

    public Matches save(MatchesRequestDTO matchDTO) {
        Team teamA = teamService.getByTeamname(matchDTO.getTeamOne());
        Team teamB = teamService.getByTeamname(matchDTO.getTeamTwo());

        if (teamA == null || teamB == null){
            log.debug("TeamA: {} or TeamB: {} not found!", teamA, teamB);

            throw new ObjectNotFoundException(Team.class, "Team not found");
        }

        boolean teammateInBothTeams = teamA.getTeammates().stream()
                .anyMatch(teamB.getTeammates()::contains);

        if(teammateInBothTeams){
            log.debug("Teammate can't be part of both teams in a single match!");

            throw new ObjectAlreadyExistsException(Teammate.class, "Teammate can't be part of both teams in a single match");
        }

        Matches match = new Matches();
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setTeamAScore(matchDTO.getTeamOneScore());
        match.setTeamBScore(matchDTO.getTeamTwoScore());

        matchOutcome(match, teamA, teamB);

        return matchesRepository.save(match);
    }

    public void matchOutcome(Matches match, Team existingTeamA, Team existingTeamB){
        log.debug("calling method matchOutcome in MatchesServiceImpl");
        if (match.getTeamAScore() > match.getTeamBScore()) {
            match.setWinner(match.getTeamA().getTeamName());
            existingTeamA.setWins(existingTeamA.getWins() + 1);

            increaseTotalGamesPlayed(existingTeamA);
            increaseTotalGamesPlayed(existingTeamB);

            updateWinningTeammates(existingTeamA);
            updateLosingTeammates(existingTeamB);

            teamService.save(existingTeamB);
            teamService.save(existingTeamA);

        } else if (match.getTeamAScore() < match.getTeamBScore()){
            match.setWinner(match.getTeamB().getTeamName());
            existingTeamB.setWins(existingTeamB.getWins() + 1);

            increaseTotalGamesPlayed(existingTeamA);
            increaseTotalGamesPlayed(existingTeamB);

            updateLosingTeammates(existingTeamA);
            updateWinningTeammates(existingTeamB);

            teamService.save(existingTeamB);
            teamService.save(existingTeamA);

        } else if (match.getTeamAScore().equals(match.getTeamBScore())){
            match.setWinner("draw");

            increaseTotalGamesPlayed(existingTeamA);
            increaseTotalGamesPlayed(existingTeamB);

            updateLosingTeammates(existingTeamA);
            updateLosingTeammates(existingTeamB);

            teamService.save(existingTeamB);
            teamService.save(existingTeamA);
        }
    }

    public Iterable<Matches> findAll(){
        return matchesRepository.findAll();
    }

    public Matches findMatchById(Long id) {
        log.debug("calling findMatchById method in MatchesServiceImpl");
        Optional<Matches> match = matchesRepository.findById(id);

        if(match.isEmpty()){
            log.debug("match doesnt exist with id: {}", id);

            throw new ObjectNotFoundException(Matches.class, id);
        }

        return match.get();
    }

    public void delete(Long id) {
        Matches existingMatch = findMatchById(id);
        teamService.decreaseGamesPlayedByOne(existingMatch);

        existingMatch.setTeamA(null);
        existingMatch.setTeamB(null);

        matchesRepository.deleteById(id);
    }

    private void updateWinningTeammates(Team team){

        team.getTeammates().forEach(teammate->{
            teammate.setTotalGamesPlayed(teammate.getTotalGamesPlayed() + 1);
            double winsDecimal = teammate.getWins();
            teammate.setWinRate((winsDecimal / teammate.getTotalGamesPlayed()) * 100);
            teammate.setWinRate(Precision.round(teammate.getWinRate(), 2));
            teammateService.initSave(teammate);
        });
    }

    private void updateLosingTeammates(Team team){

        team.getTeammates().forEach(teammate->{
            teammate.setTotalGamesPlayed(teammate.getTotalGamesPlayed() + 1);
            double winsDecimal = teammate.getWins();
            teammate.setWinRate((winsDecimal / teammate.getTotalGamesPlayed()) * 100);
            teammate.setWinRate(Precision.round(teammate.getWinRate(), 2));
            teammateService.initSave(teammate);
        });
    }

    private void increaseTotalGamesPlayed(Team team){
        team.setTotalGamesPlayed(team.getTotalGamesPlayed() + 1);
    }

}
