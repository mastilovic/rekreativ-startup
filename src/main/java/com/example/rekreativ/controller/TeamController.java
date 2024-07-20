package com.example.rekreativ.controller;


import com.example.rekreativ.model.dto.request.TeammateRequestDTO;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        Team team = teamService.findTeamById(id);

        return new ResponseEntity<Object>(team, HttpStatus.OK);
    }

    @RequestMapping(path = "/name", method = RequestMethod.GET)
    public ResponseEntity<?> getByName(@RequestParam("name") String name) {
        Team team = teamService.getByTeamname(name);

        return new ResponseEntity<Object>(team, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Team team) {
        Team newTeam = teamService.save(team);

        return new ResponseEntity<Object>(newTeam, HttpStatus.CREATED);
    }

    //FIXME: something is not right with this logic
    // fix it
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/score", method = RequestMethod.POST)
    public ResponseEntity<?> updateTeamScore(@RequestBody Team team) {
        Team existingTeam = teamService.getByTeamname(team.getTeamName());

        existingTeam.setWins(team.getWins());
        teamService.save(existingTeam);

        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/add_teammate", method = RequestMethod.POST)
    public ResponseEntity<?> addTeammateToTeam(@RequestBody TeammateRequestDTO teammateDTO) {
        Team team = teamService.addTeammateToTeam(teammateDTO.getTeamName(), teammateDTO.getTeamMate());

        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/teammate/{teamname}/{teammate}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteTeammateFromTeam(@PathVariable("teamname") String teamname,
                                                    @PathVariable("teammate") String teammate) {

        teamService.deleteTeammateFromTeam(teamname, teammate);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //FIXME: something is not right with this logic
    // fix it
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateTeam(@RequestBody Team team) {
        Team existingTeam = teamService.findTeamById(team.getId());

        //todo: create update method for team
        existingTeam.setTeamName(team.getTeamName());
        existingTeam.setCity(team.getCity());
        existingTeam.setWins(team.getWins());
        Team update = teamService.save(existingTeam);

        return new ResponseEntity<>(update, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Iterable<Team> teams = teamService.findAll();

        return new ResponseEntity<Object>(teams, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        teamService.delete(id);

        return new ResponseEntity<Object>("Team deleted successfully!", HttpStatus.OK);
    }
}