package com.example.rekreativ.controller;

import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.model.dto.TeammateUpdateDTO;
import com.example.rekreativ.service.TeammateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/teammate")
public class TeammateController {

    private final TeammateService teammateService;

    @Autowired
    public TeammateController(TeammateService teammateService) {
        this.teammateService = teammateService;
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Teammate teammate) {
        Teammate newTeammate = teammateService.save(teammate);

        return new ResponseEntity<Object>(newTeammate, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Iterable<Teammate> teammates = teammateService.findAll();

        return new ResponseEntity<Object>(teammates, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/name", method = RequestMethod.GET)
    public ResponseEntity<?> getByName(@RequestParam("name") String name) {
        Teammate teammate = teammateService.findTeammateByName(name);

        return new ResponseEntity<Object>(teammate, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Teammate teammate = teammateService.findById(id);

        return new ResponseEntity<Object>(teammate, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTeammate(@PathVariable Long id) {
        teammateService.delete(id);

        return new ResponseEntity<Object>("Teammate deleted successfully!", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Teammate> updateTeammate(@RequestBody TeammateUpdateDTO teammateUpdateDTO) {
        Teammate teammate = teammateService.update(teammateUpdateDTO);

        return new ResponseEntity<>(teammate, HttpStatus.OK);
    }
}
