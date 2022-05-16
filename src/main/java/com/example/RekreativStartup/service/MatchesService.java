package com.example.RekreativStartup.service;

import com.example.RekreativStartup.model.Matches;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.Teammate;
import com.example.RekreativStartup.repository.MatchesRepository;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.repository.TeammateRepository;
import com.example.RekreativStartup.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchesService {


    private final MatchesRepository matchesRepository;
    private final TeamRepository teamRepository;
    private final TeammateService teammateService;
    private final ValidatorUtil validatorUtil;
    private final TeamService teamService;
    private final TeammateRepository teammateRepository;

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

    public void matchOutcome(Matches newMatch, Team existingTeamOne, Team existingTeamTwo){
        if (newMatch.getTeamAScore() > newMatch.getTeamBScore()) {
            newMatch.setWinner(newMatch.getTeamA().getTeamName());

            existingTeamOne.setWins(existingTeamOne.getWins() + 1);
            existingTeamOne.setTotalGamesPlayed(existingTeamOne.getTotalGamesPlayed() + 1);
            existingTeamTwo.setTotalGamesPlayed(existingTeamTwo.getTotalGamesPlayed() + 1);

            Collection<Teammate> myTeammates = existingTeamOne.getTeammates();
            for (Teammate t:myTeammates) {
                Integer myInt = t.getTotalGamesPlayed();
                myInt = myInt==null?0:myInt;
                t.setTotalGamesPlayed(myInt + 1);

                Integer myWins = t.getWins();
                myWins = myWins==null?0:myWins;
                t.setWins(myWins + 1);

                teammateRepository.save(t);
            }

            teamService.save(existingTeamTwo);
            teamService.save(existingTeamOne);

        } else if (newMatch.getTeamAScore() < newMatch.getTeamBScore()){
            newMatch.setWinner(newMatch.getTeamB().getTeamName());

            existingTeamTwo.setWins(existingTeamTwo.getWins() + 1);

            Collection<Teammate> myTeammates = existingTeamTwo.getTeammates();
            for (Teammate t:myTeammates) {
                Integer myInt = t.getTotalGamesPlayed();
                myInt = myInt==null?0:myInt;
                t.setTotalGamesPlayed(myInt + 1);

                Integer myWins = t.getWins();
                myWins = myWins==null?0:myWins;
                t.setWins(myWins + 1);

                teammateRepository.save(t);
            }

            existingTeamOne.setTotalGamesPlayed(existingTeamOne.getTotalGamesPlayed() + 1);
            existingTeamTwo.setTotalGamesPlayed(existingTeamTwo.getTotalGamesPlayed() + 1);
            teamService.save(existingTeamTwo);
            teamService.save(existingTeamOne);
        } else if (Objects.equals(newMatch.getTeamAScore(), newMatch.getTeamBScore())){
            newMatch.setWinner("draw");

            existingTeamOne.setTotalGamesPlayed(existingTeamOne.getTotalGamesPlayed() + 1);
            existingTeamTwo.setTotalGamesPlayed(existingTeamTwo.getTotalGamesPlayed() + 1);
            teamService.save(existingTeamTwo);
            teamService.save(existingTeamOne);
        }
    }

    public Matches createNewMatchup(String teamOne, String teamTwo, Integer teamOneScore, Integer teamTwoScore){
        Optional<Team> optionalTeamA = teamRepository.findByTeamName(teamOne);
        Optional<Team> optionalTeamB = teamRepository.findByTeamName(teamTwo);
        Team teamA = optionalTeamA.get();
        Team teamB = optionalTeamB.get();
//
//        ArrayList<String> teammatesInTeamAList = new ArrayList<String>();
//        ArrayList<String> teammatesInTeamBList = new ArrayList<String>();
//
//        ArrayList<Integer> teamAScoreList = new ArrayList<Integer>();
//        Integer teamAScore = validatorUtil.getScoresFromTeammates(teamOne);
//        Integer teamBScore = validatorUtil.getScoresFromTeammates(teamTwo);

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

}
