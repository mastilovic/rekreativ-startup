package com.example.RekreativStartup.Service;

import com.example.RekreativStartup.model.Matches;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.Teammate;
import com.example.RekreativStartup.repository.MatchesRepository;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class MatchesService {


    private final MatchesRepository matchesRepository;
    private final TeamRepository teamRepository;
    private final TeammateService teammateService;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public MatchesService(MatchesRepository matchesRepository,
                          TeamRepository teamRepository,
                          TeammateService teammateService,
                          ValidatorUtil validatorUtil) {
        this.matchesRepository = matchesRepository;
        this.teamRepository = teamRepository;
        this.teammateService = teammateService;
        this.validatorUtil = validatorUtil;
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
}
