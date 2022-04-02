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
    public ResponseEntity<?> createNewMatches(@RequestParam(value = "teama", required = false) String teama,
                                              @RequestParam(value = "teamb", required = false) String teamb,
                                              @RequestParam(value = "teamascore", required = false) Integer teamascore,
                                              @RequestParam(value = "teambscore", required = false) Integer teambscore) {
        Team existingTeamOne = teamService.getByTeamname(teama).get();
        Team existingTeamTwo = teamService.getByTeamname(teamb).get();
        if (ValidatorUtil.teamValidator(existingTeamOne) ||
                ValidatorUtil.teamValidator(existingTeamTwo)){
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
        Matches newMatchup = matchesService.createNewMatchup(teama, teamb, teamascore, teambscore);

        return new ResponseEntity<Object>(newMatchup, HttpStatus.CREATED);
    }

//    @RequestParam(value = "teamname", required = false) String teamname,
}
