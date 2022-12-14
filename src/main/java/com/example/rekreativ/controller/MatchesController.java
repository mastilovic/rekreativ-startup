package com.example.rekreativ.controller;

import com.example.rekreativ.forms.MatchesToMatchForm;
import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.service.MatchesService;
import com.example.rekreativ.service.TeamService;
import com.example.rekreativ.service.impl.TeamServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/matches/v1")
@Slf4j
public class MatchesController {

    private final MatchesService matchesService;
    private final TeamService teamService;

    public MatchesController(MatchesService matchesService,
                             TeamServiceImpl teamService) {
        this.matchesService = matchesService;
        this.teamService = teamService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/create/matches", method = RequestMethod.POST)
    public ResponseEntity<?> createNewMatch(@RequestBody MatchesToMatchForm match) {
        Team existingTeamOne = teamService.getByTeamname(match.getTeamOne());
        Team existingTeamTwo = teamService.getByTeamname(match.getTeamTwo());

        if (existingTeamOne == null || existingTeamTwo == null){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        for (Teammate t:existingTeamOne.getTeammates()){
            if(existingTeamTwo.getTeammates().contains(t)){

                return new ResponseEntity<Object>(
                        "One player can't be part of both teams in a single match!",
                        HttpStatus.BAD_REQUEST);
            }
        }
        
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
        Matches match = matchesService.findMatchById(id);

        return new ResponseEntity<Object>(match, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        matchesService.delete(id);

        return new ResponseEntity<Object>("Match deleted successfully!", HttpStatus.OK);
    }
}