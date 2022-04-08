package com.example.RekreativStartup.Service;

import com.example.RekreativStartup.model.Matches;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.Teammate;
import com.example.RekreativStartup.model.User;
import com.example.RekreativStartup.repository.MatchesRepository;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class MatchesService {


    private final MatchesRepository matchesRepository;
    private final TeamRepository teamRepository;
    private final TeammateService teammateService;
    private final ValidatorUtil validatorUtil;
    private final TeamService teamService;

    @Autowired
    public MatchesService(MatchesRepository matchesRepository,
                          TeamRepository teamRepository,
                          TeammateService teammateService,
                          ValidatorUtil validatorUtil,
                          TeamService teamService) {
        this.matchesRepository = matchesRepository;
        this.teamRepository = teamRepository;
        this.teammateService = teammateService;
        this.validatorUtil = validatorUtil;
        this.teamService = teamService;
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

    public Matches getWinner(String teamOne, String teamTwo, Long matchId){
        Team existingTeamOne = teamService.getByTeamname(teamOne).get();
        Team existingTeamTwo = teamService.getByTeamname(teamTwo).get();
        Matches existingMatch = findMatchById(matchId).get();

        if (existingTeamOne.getScore() > existingTeamTwo.getScore()) {
            existingMatch.setWinner(existingMatch.getTeamA().getTeamName());
            int myTeamAResult = existingMatch.getTeamA().getScore();
            myTeamAResult++;
            existingMatch.getTeamA().setScore(myTeamAResult);

            int myTeamBResult = existingMatch.getTeamB().getScore();
            myTeamBResult--;
            existingMatch.getTeamB().setScore(myTeamBResult);

        } else if (existingMatch.getTeamAScore() < existingMatch.getTeamBScore()){
            existingMatch.setWinner(existingMatch.getTeamB().getTeamName());

            int myTeamBResult = existingMatch.getTeamB().getScore();
            myTeamBResult++;
            existingMatch.getTeamB().setScore(myTeamBResult);

            int myTeamAResult = existingMatch.getTeamA().getScore();
            myTeamAResult--;
            existingMatch.getTeamA().setScore(myTeamAResult);
        } else if (Objects.equals(existingMatch.getTeamAScore(), existingMatch.getTeamBScore())){
            existingMatch.setWinner("draw");
        }
        return save(existingMatch);
    }
}
