package com.example.RekreativStartup.controller;


import com.example.RekreativStartup.model.Teammate;
import com.example.RekreativStartup.service.TeamService;
import com.example.RekreativStartup.service.TeammateService;
import com.example.RekreativStartup.service.UserService;
import com.example.RekreativStartup.forms.TeammateToTeamForm;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.repository.TeammateRepository;
import com.example.RekreativStartup.repository.UserRepository;
import com.example.RekreativStartup.util.JwtUtil;
import com.example.RekreativStartup.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/team/v1")
public class TeamController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeammateRepository teammateRepository;
    private final TeammateService teammateService;
    private final TeamService teamService;

    @Autowired
    public TeamController(JwtUtil jwtUtil,
                          UserRepository userRepository,
                          UserService userService,
                          TeamRepository teamRepository,
                          TeammateRepository teammateRepository,
                          TeammateService teammateService, TeamService teamService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService;
        this.teamRepository = teamRepository;
        this.teammateRepository = teammateRepository;
        this.teammateService = teammateService;
        this.teamService = teamService;
    }



    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {

        Team obj = teamService.findTeamById(id).orElse(null);
        if (obj == null) {

            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Object>(obj, HttpStatus.OK);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Team team) {

        if(ValidatorUtil.teamValidator(team)){

            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        Team newTeam = new Team();
        newTeam.setTeamName(team.getTeamName());
        newTeam.setCity(team.getCity());
        newTeam.setWins(0);
        newTeam.setTotalGamesPlayed(0);
        teamService.save(newTeam);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/update/score", method = RequestMethod.POST)
    public ResponseEntity<?> updateTeamScore(@RequestBody Team team) {

        Team existingTeam = teamService.getByTeamname(team.getTeamName()).orElse(null);
        if (existingTeam == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }
        existingTeam.setWins(team.getWins());
        teamService.save(existingTeam);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/add/teammate", method = RequestMethod.POST)
    public ResponseEntity<?> addTeammateToTeam(@RequestBody TeammateToTeamForm form){

        teamService.addTeammateToTeam(form.getTeamName(), form.getTeamMate());

        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/teammate/{teamname}/{teammate}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteTeammateFromTeam(@PathVariable("teamname") String teamname,
                                                    @PathVariable("teammate") String teammate) {

        Team existingTeam = teamService.getByTeamname(teamname).orElse(null);
        Teammate existingTeammate = teammateService.findTeammateByName(teammate).orElse(null);
        if (existingTeam == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }
        existingTeam.getTeammates().remove(existingTeammate);
        teamService.save(existingTeam);

        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateTeam(@RequestBody Team team) {

        Team existingTeam = teamService.findTeamById(team.getId()).orElse(null);
        if (existingTeam == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }

        existingTeam.setTeamName(team.getTeamName());
        existingTeam.setCity(team.getCity());
        existingTeam.setWins(team.getWins());
        teamService.save(existingTeam);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {

        Iterable<Team> obj = teamService.findAll();
        if (obj == null) {

            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }

        List<Team> myTeams = StreamSupport.stream(obj.spliterator(), false)
                .collect(Collectors.toList());

        return new ResponseEntity<Object>(myTeams, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        teamService.delete(id);

        return new ResponseEntity<Object>("Team deleted successfully!", HttpStatus.OK);
    }
}