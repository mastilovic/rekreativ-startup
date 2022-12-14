package com.example.RekreativStartup.service;

import com.example.RekreativStartup.model.Matches;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.repository.MatchesRepository;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.repository.TeammateRepository;
import com.example.RekreativStartup.util.ValidatorUtil;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Optional;


@Service
public class MatchesService {


    private final MatchesRepository matchesRepository;
    private final TeamRepository teamRepository;
    private final TeammateService teammateService;
    private final ValidatorUtil validatorUtil;
    private final TeamService teamService;
    private final TeammateRepository teammateRepository;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Autowired
    public MatchesService(MatchesRepository matchesRepository,
                          TeamRepository teamRepository,
                          TeammateService teammateService,
                          ValidatorUtil validatorUtil,
                          TeamService teamService,
                          TeammateRepository teammateRepository) {
        this.matchesRepository = matchesRepository;
        this.teamRepository = teamRepository;
        this.teammateService = teammateService;
        this.validatorUtil = validatorUtil;
        this.teamService = teamService;
        this.teammateRepository = teammateRepository;
    }


    public void matchOutcome(Matches newMatch, Team existingTeamA, Team existingTeamB){
        // case when teamA wins
        if (newMatch.getTeamAScore() > newMatch.getTeamBScore()) {
            newMatch.setWinner(newMatch.getTeamA().getTeamName());
            existingTeamA.setWins(existingTeamA.getWins() + 1);
            existingTeamA.setTotalGamesPlayed(existingTeamA.getTotalGamesPlayed() + 1);
            existingTeamB.setTotalGamesPlayed(existingTeamB.getTotalGamesPlayed() + 1);

            updateTeammatesThatDidntWin(existingTeamB);
            updateTeammatesThatWon(existingTeamA);

            teamService.save(existingTeamB);
            teamService.save(existingTeamA);

        // case when teamB wins
        } else if (newMatch.getTeamAScore() < newMatch.getTeamBScore()){
            newMatch.setWinner(newMatch.getTeamB().getTeamName());
            existingTeamB.setWins(existingTeamB.getWins() + 1);
            existingTeamA.setTotalGamesPlayed(existingTeamA.getTotalGamesPlayed() + 1);
            existingTeamB.setTotalGamesPlayed(existingTeamB.getTotalGamesPlayed() + 1);

            updateTeammatesThatDidntWin(existingTeamA);
            updateTeammatesThatWon(existingTeamB);

            teamService.save(existingTeamB);
            teamService.save(existingTeamA);

        // case when its a draw
        } else if (newMatch.getTeamAScore().equals(newMatch.getTeamBScore())){
            newMatch.setWinner("draw");
            existingTeamA.setTotalGamesPlayed(existingTeamA.getTotalGamesPlayed() + 1);
            existingTeamB.setTotalGamesPlayed(existingTeamB.getTotalGamesPlayed() + 1);

            updateTeammatesThatDidntWin(existingTeamA);
            updateTeammatesThatDidntWin(existingTeamB);

            teamService.save(existingTeamB);
            teamService.save(existingTeamA);
        }
    }

    public Matches createNewMatchup(String teamOne, String teamTwo, Integer teamOneScore, Integer teamTwoScore){
        Optional<Team> optionalTeamA = teamRepository.findByTeamName(teamOne);
        Optional<Team> optionalTeamB = teamRepository.findByTeamName(teamTwo);
        Team teamA = optionalTeamA.get();
        Team teamB = optionalTeamB.get();

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

    public Optional<Matches> findMatchById(Long id) {
        return matchesRepository.findById(id);
    }

    public void delete(Long id) {
        matchesRepository.deleteById(id);
    }

    private void updateTeammatesThatWon(Team team){
        team.getTeammates().forEach(teammate->{
            teammate.setTotalGamesPlayed(teammate.getTotalGamesPlayed() + 1);
            double winsDecimal = teammate.getWins();
            teammate.setWinRate((winsDecimal / teammate.getTotalGamesPlayed()) * 100);
            teammate.setWinRate(Precision.round(teammate.getWinRate(), 2));
            teammateService.save(teammate);
        });
    }

    private void updateTeammatesThatDidntWin(Team team){
        team.getTeammates().forEach(teammate->{
            teammate.setTotalGamesPlayed(teammate.getTotalGamesPlayed() + 1);
            double winsDecimal = teammate.getWins();
            teammate.setWinRate((winsDecimal / teammate.getTotalGamesPlayed()) * 100);
            teammate.setWinRate(Precision.round(teammate.getWinRate(), 2));
            teammateService.save(teammate);
        });
    }

}
