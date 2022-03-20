package com.example.RekreativStartup.controller;


import com.example.RekreativStartup.Service.TeamService;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/team/v1")
public class TeamController {

    @Autowired
    private TeamService teamService;

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
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Team team) {
        Team newTeam = new Team();
        newTeam.setTeamName(team.getTeamName());
//        newTeam.setCaptain(team.getCaptain());
        newTeam.setCity(team.getCity());
        newTeam.setUser(team.getUser());
        newTeam.setScore(null);
        newTeam = teamService.save(newTeam);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }
    @RequestMapping(path = "/update/score", method = RequestMethod.POST)
    public ResponseEntity<?> updateTeamScore(@RequestBody Team team) {

        Team existingTeam = teamService.findByTeamname(team.getTeamName()).orElse(null);
        if (existingTeam == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }
        existingTeam.setScore(team.getScore());
        teamService.save(existingTeam);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }


    // teamname, user, captain, city, score
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateTeam(@RequestBody Team team) {

        Team existingTeam = teamService.findById(team.getId()).orElse(null);
        if (existingTeam == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }

        existingTeam.setTeamName(team.getTeamName());
        existingTeam.setUser(team.getUser());
//        existingTeam.setCaptain(team.getCaptain());
        existingTeam.setCity(team.getCity());
        existingTeam.setScore(team.getScore());
        teamService.save(existingTeam);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

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
