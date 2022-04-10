package com.example.RekreativStartup.controller;

import com.example.RekreativStartup.Service.MatchesService;
import com.example.RekreativStartup.Service.TeamService;
import com.example.RekreativStartup.forms.MatchesToMatchForm;
import com.example.RekreativStartup.model.Matches;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.repository.MatchesRepository;
import com.example.RekreativStartup.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/matches/v1")
@Slf4j
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
        Matches newMatch = new Matches();
        newMatch.setTeamA(existingTeamOne);
        newMatch.setTeamB(existingTeamTwo);
        newMatch.setTeamAScore(match.getTeamOneScore());
        newMatch.setTeamBScore(match.getTeamTwoScore());

        matchesService.matchOutcome(newMatch, existingTeamOne, existingTeamTwo);
        matchesService.save(newMatch);

        return new ResponseEntity<Object>(newMatch, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {

        Iterable<Matches> obj = matchesService.findAll();
        return new ResponseEntity<Object>(obj, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {

        Matches obj = matchesService.findMatchById(id).orElse(null);
        if (obj == null) {

            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Object>(obj, HttpStatus.OK);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Matches existingMatch = matchesService.findMatchById(id).get();
        teamService.decreaseGamesPlayedByOne(existingMatch);

        existingMatch.setTeamA(null);
        existingMatch.setTeamB(null);
        matchesService.delete(id);
        return new ResponseEntity<Object>("Match deleted successfully!", HttpStatus.OK);
    }
}