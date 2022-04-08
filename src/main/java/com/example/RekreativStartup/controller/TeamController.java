package com.example.RekreativStartup.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.RekreativStartup.Service.TeamService;
import com.example.RekreativStartup.Service.TeammateService;
import com.example.RekreativStartup.Service.UserService;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.Teammate;
import com.example.RekreativStartup.model.User;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.repository.TeammateRepository;
import com.example.RekreativStartup.repository.UserRepository;
import com.example.RekreativStartup.util.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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

    @Autowired
    public TeamController(JwtUtil jwtUtil,
                          UserRepository userRepository,
                          UserService userService,
                          TeamRepository teamRepository,
                          TeammateRepository teammateRepository,
                          TeammateService teammateService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService;
        this.teamRepository = teamRepository;
        this.teammateRepository = teammateRepository;
        this.teammateService = teammateService;
    }

    @Autowired
    private TeamService teamService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {

        Team obj = teamService.findById(id).orElse(null);
        if (obj == null) {

            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
//
//        ModelMapper modelMapper = new ModelMapper();
//        UserDTO userDto = modelMapper.map(obj, UserDTO.class);

        return new ResponseEntity<Object>(obj, HttpStatus.OK);

    }
    //long id, str teamname, str[] teammates, str captain, str city, int score
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Team team) {

        Team newTeam = new Team();

        newTeam.setTeamName(team.getTeamName());
        newTeam.setCity(team.getCity());
//        newTeam.setTeammate(null);
        newTeam.setScore(team.getScore());
        newTeam = teamService.save(newTeam);
        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/update/score", method = RequestMethod.POST)
    public ResponseEntity<?> updateTeamScore(@RequestBody Team team) {

        Team existingTeam = teamService.getByTeamname(team.getTeamName()).orElse(null);
        if (existingTeam == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }
        existingTeam.setScore(team.getScore());
        teamService.save(existingTeam);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/add/teammate", method = RequestMethod.POST)
    public ResponseEntity<?> addTeammateToTeam(@RequestBody TeammateToTeamForm form){

        teamService.addTeammateToTeam(form.getTeamName(), form.getTeamMate());
        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }


//    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
//    @RequestMapping(path = "/add/user", method = RequestMethod.POST)
//    public ResponseEntity<?> addUserToTeam(@RequestParam(value = "teamname", required = false) String teamname,
//                                           @RequestParam(value = "teammate", required = false) String teammate) {
//
//        Team existingTeam = teamService.findByTeamname(teamname).orElse(null);
//        if (existingTeam == null) {
//            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
//        }
//
//        existingTeam.getTeammate().add(teammate);
//
//        teamService.save(existingTeam);
//
//        return new ResponseEntity<Object>(HttpStatus.CREATED);
//    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/user/{teammate}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteUserFromTeam(@PathVariable("teammate") String teammate) {

        Team existingTeamate = teamService.getByTeamname(teammate).orElse(null);
        if (existingTeamate == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }

        teamService.deleteTeammate(teammate);

        return new ResponseEntity<Object>(HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
//    @RequestMapping(path = "/add/user", method = RequestMethod.POST)
//    public ResponseEntity<?> addUser(@RequestBody Team team) {
//
//        Team existingTeam = teamService.findByTeamname(team.getTeamName()).orElse(null);
//        if (existingTeam == null) {
//            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
//        }
//        existingTeam.setUser(team.getUser());
//
//        teamService.save(existingTeam);
//
//        return new ResponseEntity<Object>(HttpStatus.CREATED);
//    }

    // teamname, user, captain, city, score
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateTeam(@RequestBody Team team) {

        Team existingTeam = teamService.findById(team.getId()).orElse(null);
        if (existingTeam == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }

        existingTeam.setTeamName(team.getTeamName());
        existingTeam.setCity(team.getCity());
        existingTeam.setScore(team.getScore());
        teamService.save(existingTeam);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {

        Iterable<Team> obj = teamService.findAll();
        ArrayList myList = new ArrayList();
        for (Team t: obj){
            myList.add(t);
        }
        if (obj == null) {

            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
//        Object[] objArray = myList.toArray();
//        ArrayList dtoList = new ArrayList();
//        ModelMapper modelMapper = new ModelMapper();
//        for(int i=0; i < objArray.length ; i++) {
//            UserDTO userDto = modelMapper.map(objArray[i], UserDTO.class);
//            dtoList.add(userDto);
//        }

        return new ResponseEntity<Object>(obj, HttpStatus.OK);
    }
}

class TeammateToTeamForm{
    private String teamName;
    private String teamMate;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamMate() {
        return teamMate;
    }

    public void setTeamMate(String teamMate) {
        this.teamMate = teamMate;
    }
}