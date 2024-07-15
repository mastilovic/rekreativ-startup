package com.example.rekreativ.controller;

import com.example.rekreativ.model.dto.request.MatchesRequestDTO;
import com.example.rekreativ.model.Matches;
import com.example.rekreativ.service.MatchesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/matches")
@Slf4j
public class MatchesController {

    private final MatchesService matchesService;

    public MatchesController(MatchesService matchesService) {
        this.matchesService = matchesService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/create/matches", method = RequestMethod.POST)
    public ResponseEntity<?> createNewMatch(@RequestBody MatchesRequestDTO match) {

        Matches newMatch = matchesService.save(match);

        return new ResponseEntity<>(newMatch, HttpStatus.CREATED);
    }

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Iterable<Matches> matches = matchesService.findAll();

        return new ResponseEntity<Object>(matches, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        Matches match = matchesService.findMatchById(id);

        return new ResponseEntity<Object>(match, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        matchesService.delete(id);

        return new ResponseEntity<>("Match deleted successfully!", HttpStatus.OK);
    }
}