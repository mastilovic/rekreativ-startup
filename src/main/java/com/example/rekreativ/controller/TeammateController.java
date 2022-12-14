package com.example.rekreativ.controller;

import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.service.TeammateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/teammate/v1")
public class TeammateController {

    private final TeammateService teammateService;

    @Autowired
    public TeammateController(TeammateService teammateService) {
        this.teammateService = teammateService;
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Teammate teammate) throws ValidationException {
        Teammate newTeammate = teammateService.save(teammate);

        return new ResponseEntity<Object>(newTeammate, HttpStatus.CREATED);
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Iterable<Teammate> obj = teammateService.findAll();

        return new ResponseEntity<Object>(obj, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTeammate(@PathVariable Long id) {
        teammateService.delete(id);

        return new ResponseEntity<Object>("Teammate deleted successfully!", HttpStatus.OK);
    }
}
