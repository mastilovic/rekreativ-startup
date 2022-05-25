package com.example.RekreativStartup.controller;

import com.example.RekreativStartup.service.TeammateService;
import com.example.RekreativStartup.model.Teammate;
import com.example.RekreativStartup.repository.TeammateRepository;
import com.example.RekreativStartup.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/teammate/v1")
public class TeammateController {

    private final TeammateRepository teammateRepository;
    private final TeammateService teammateService;

    @Autowired
    public TeammateController(TeammateRepository teammateRepository,
                              TeammateService teammateService) {
        this.teammateRepository = teammateRepository;
        this.teammateService = teammateService;
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Teammate teammate) {
        if(ValidatorUtil.teammateValidator(teammate)){

            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        teammate.setName(teammate.getName());
        teammate.setTotalGamesPlayed(0);
        teammate.setWins(0);
        teammateRepository.save(teammate);

        return new ResponseEntity<Object>(teammate, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Iterable<Teammate> obj = teammateService.findAll();
        if (obj == null) {

            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Object>(obj, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTeammate(@PathVariable Long id) {
        teammateService.delete(id);

        return new ResponseEntity<Object>("Teammate deleted successfully!", HttpStatus.OK);
    }
}
