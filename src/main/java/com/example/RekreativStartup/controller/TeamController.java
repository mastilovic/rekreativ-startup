package com.example.RekreativStartup.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.RekreativStartup.Service.TeamService;
import com.example.RekreativStartup.Service.UserService;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.User;
import com.example.RekreativStartup.repository.UserRepository;
import com.example.RekreativStartup.util.JwtUtil;
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

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/team/v1")
public class TeamController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public TeamController(JwtUtil jwtUtil, UserRepository userRepository, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService;
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
    public ResponseEntity<?> register(@RequestBody Team team, HttpServletRequest request) {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = jwtUtil.decodedToken(token);
        String username = decodedJWT.getSubject();

        User existingUser = userRepository.findByUsername(username).orElseThrow(null);
//        List<User> myList = new ArrayList<>();
//        myList.add(existingUser);

        Team newTeam = new Team();

        newTeam.setTeamName(team.getTeamName());
        newTeam.setCity(team.getCity());
        newTeam.getTeammate().add(username);
//        newTeam.setTeammate(Arrays.asList(username));
        newTeam = teamService.save(newTeam);

        existingUser.getTeam().add(team.getTeamName());
        userService.saveUser(existingUser);
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
    @RequestMapping(path = "/add/user", method = RequestMethod.POST)
    public ResponseEntity<?> addUserToTeam(@RequestBody TeammateToTeamForm team) {

        Team existingTeam = teamService.getByTeamname(team.getTeamName()).orElse(null);
        if (existingTeam == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }

//        existingTeam.getTeammate().add(teammate);

        teamService.addTeammateToTeam(team.getTeamName(), team.getTeamMate());

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
//        existingTeam.setUser(team.getUser());
//        existingTeam.setCaptain(team.getCaptain());
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