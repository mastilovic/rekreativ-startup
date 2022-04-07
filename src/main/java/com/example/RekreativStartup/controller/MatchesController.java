package com.example.RekreativStartup.controller;

import com.example.RekreativStartup.Service.MatchesService;
import com.example.RekreativStartup.Service.TeamService;
import com.example.RekreativStartup.model.Matches;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.repository.MatchesRepository;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/matches/v1")
public class MatchesController {

    private final TeamRepository teamRepository;
    private final MatchesRepository matchesRepository;
    private final MatchesService matchesService;
    private final TeamService teamService;

    @Autowired
    public MatchesController(TeamRepository teamRepository,
                             MatchesRepository matchesRepository,
                             MatchesService matchesService,
                             TeamService teamService) {

        this.teamRepository = teamRepository;
        this.matchesRepository = matchesRepository;
        this.matchesService = matchesService;
        this.teamService = teamService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/create/matches", method = RequestMethod.POST)
    public ResponseEntity<?> createNewMatch(@RequestBody MatchesToMatchForm match) {
        Team existingTeamOne = teamService.getByTeamname(match.getTeamOne()).get();
        Team existingTeamTwo = teamService.getByTeamname(match.getTeamTwo()).get();
//        if (ValidatorUtil.teamValidator(existingTeamOne) ||
//                ValidatorUtil.teamValidator(existingTeamTwo)){
//            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
//        }
        Matches newMatch = new Matches();
        newMatch.setTeamA(existingTeamOne);
        newMatch.setTeamB(existingTeamTwo);
        newMatch.setTeamAScore(match.getTeamOneScore());
        newMatch.setTeamBScore(match.getTeamTwoScore());
        matchesService.save(newMatch);

        return new ResponseEntity<Object>(newMatch, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {

        Iterable<Matches> obj = matchesService.findAll();
        return new ResponseEntity<Object>(obj, HttpStatus.OK);
    }
}

class MatchesToMatchForm{
    private String teamOne;
    private String teamTwo;
    private Integer teamOneScore;
    private Integer teamTwoScore;

    public String getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(String teamOne) {
        this.teamOne = teamOne;
    }

    public String getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(String teamTwo) {
        this.teamTwo = teamTwo;
    }

    public Integer getTeamOneScore() {
        return teamOneScore;
    }

    public void setTeamOneScore(Integer teamOneScore) {
        this.teamOneScore = teamOneScore;
    }

    public Integer getTeamTwoScore() {
        return teamTwoScore;
    }

    public void setTeamTwoScore(Integer teamTwoScore) {
        this.teamTwoScore = teamTwoScore;
    }
}