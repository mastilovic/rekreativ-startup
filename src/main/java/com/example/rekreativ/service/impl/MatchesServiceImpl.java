package com.example.rekreativ.service.impl;

import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.repository.MatchesRepository;
import com.example.rekreativ.service.MatchesService;
import com.example.rekreativ.service.TeamService;
import com.example.rekreativ.service.TeammateService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Optional;


@Service
public class MatchesServiceImpl implements MatchesService {

    private final MatchesRepository matchesRepository;
    private final TeammateService teammateService;
    private final TeamService teamService;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Autowired
    public MatchesServiceImpl(MatchesRepository matchesRepository,
                              TeammateServiceImpl teammateServiceImpl,
                              TeamServiceImpl teamService) {
        this.matchesRepository = matchesRepository;
        this.teammateService = teammateServiceImpl;
        this.teamService = teamService;
    }


    public void matchOutcome(Matches newMatch, Team existingTeamA, Team existingTeamB){
        // case when teamA wins
        if (newMatch.getTeamAScore() > newMatch.getTeamBScore()) {
            newMatch.setWinner(newMatch.getTeamA().getTeamName());
            existingTeamA.setWins(existingTeamA.getWins() + 1);
            existingTeamA.setTotalGamesPlayed(existingTeamA.getTotalGamesPlayed() + 1);
            existingTeamB.setTotalGamesPlayed(existingTeamB.getTotalGamesPlayed() + 1);

            updateTeammatesThatLost(existingTeamB);
            updateTeammatesThatWon(existingTeamA);

            teamService.save(existingTeamB);
            teamService.save(existingTeamA);

        // case when teamB wins
        } else if (newMatch.getTeamAScore() < newMatch.getTeamBScore()){
            newMatch.setWinner(newMatch.getTeamB().getTeamName());
            existingTeamB.setWins(existingTeamB.getWins() + 1);
            existingTeamA.setTotalGamesPlayed(existingTeamA.getTotalGamesPlayed() + 1);
            existingTeamB.setTotalGamesPlayed(existingTeamB.getTotalGamesPlayed() + 1);

            updateTeammatesThatLost(existingTeamA);
            updateTeammatesThatWon(existingTeamB);

            teamService.save(existingTeamB);
            teamService.save(existingTeamA);

        // case when its a draw
        } else if (newMatch.getTeamAScore().equals(newMatch.getTeamBScore())){
            newMatch.setWinner("draw");
            existingTeamA.setTotalGamesPlayed(existingTeamA.getTotalGamesPlayed() + 1);
            existingTeamB.setTotalGamesPlayed(existingTeamB.getTotalGamesPlayed() + 1);

            updateTeammatesThatLost(existingTeamA);
            updateTeammatesThatLost(existingTeamB);

            teamService.save(existingTeamB);
            teamService.save(existingTeamA);
        }
    }

    public Matches createNewMatchup(String teamOne, String teamTwo, Integer teamOneScore, Integer teamTwoScore){
        Team teamA = teamService.getByTeamname(teamOne);
        Team teamB = teamService.getByTeamname(teamTwo);

        Matches newMatch = new Matches();
        newMatch.setTeamA(teamA);
        newMatch.setTeamB(teamB);
        newMatch.setTeamAScore(teamOneScore);
        newMatch.setTeamBScore(teamTwoScore);

        return matchesRepository.save(newMatch);
    }

    public Matches save(Matches matches) {
        return matchesRepository.save(matches);
    }

    public Iterable<Matches> findAll(){
        return matchesRepository.findAll();
    }

    public Matches findMatchById(Long id) {
        Optional<Matches> match = matchesRepository.findById(id);

        if(match.isEmpty()){

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

    private void updateTeammatesThatWon(Team team){

        team.getTeammates().forEach(teammate->{
            teammate.setTotalGamesPlayed(teammate.getTotalGamesPlayed() + 1);
            double winsDecimal = teammate.getWins();
            teammate.setWinRate((winsDecimal / teammate.getTotalGamesPlayed()) * 100);
            teammate.setWinRate(Precision.round(teammate.getWinRate(), 2));
            teammateService.initSave(teammate);
        });
    }

    private void updateTeammatesThatLost(Team team){
        team.getTeammates().forEach(teammate->{
            teammate.setTotalGamesPlayed(teammate.getTotalGamesPlayed() + 1);
            double winsDecimal = teammate.getWins();
            teammate.setWinRate((winsDecimal / teammate.getTotalGamesPlayed()) * 100);
            teammate.setWinRate(Precision.round(teammate.getWinRate(), 2));
            teammateService.initSave(teammate);
        });
    }

}
