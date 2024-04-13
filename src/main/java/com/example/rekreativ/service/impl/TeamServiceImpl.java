package com.example.rekreativ.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.model.User;
import com.example.rekreativ.repository.TeamRepository;
import com.example.rekreativ.service.TeamService;
import com.example.rekreativ.service.TeammateService;
import com.example.rekreativ.commons.JwtHandler;
import com.example.rekreativ.commons.CustomValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    private final JwtHandler jwtHandler;
    private final TeamRepository teamRepository;
    private final TeammateService teammateService;
    private final CustomValidator customValidator;

    public TeamServiceImpl(JwtHandler jwtHandler,
                           TeamRepository teamRepository,
                           TeammateService teammateService,
                           CustomValidator customValidator) {
        this.jwtHandler = jwtHandler;
        this.teamRepository = teamRepository;
        this.teammateService = teammateService;
        this.customValidator = customValidator;
    }

    public Iterable<Team> findAll() {
        log.debug("calling findAll method");

        return teamRepository.findAll();
    }

    public Team findTeamById(Long id) {
        log.debug("calling findTeamById method");

        return teamRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Team.class, id));
    }

    public Team getByTeamname(String teamName) {
        log.debug("calling getByTeamname method");

        return teamRepository.findByTeamName(teamName)
                .orElseThrow(() -> new ObjectNotFoundException(Team.class, teamName));
    }

    public void delete(Long id) {
        Team team = findTeamById(id);

        teamRepository.deleteById(team.getId());
    }

    public Team saveTeamWithUsername(Team team, HttpServletRequest request) {
        log.debug("calling saveTeamWithUsername method");

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = jwtHandler.decodedToken(token);
        String username = decodedJWT.getSubject();

        User user = new User();
        user.setUsername(username);
        customValidator.validate(user);

        return teamRepository.save(team);
    }

    public Team save(Team team) {
        log.info("calling save method in TeamServiceImpl");

        Optional<Team> optionalTeam = teamRepository.findByTeamName(team.getTeamName());

        if (optionalTeam.isPresent()) {
            log.error("Team already exists with name: " + team.getTeamName());

            throw new ObjectAlreadyExistsException(Team.class, team.getTeamName());
        }

        Team newTeam = new Team();
        newTeam.setTeamName(team.getTeamName());
        newTeam.setCity(team.getCity());
        newTeam.setWins(0);
        newTeam.setTotalGamesPlayed(0);
        customValidator.validate(newTeam);

        return teamRepository.save(team);
    }

    public void initSave(Team team) {
        log.debug("calling initSave method in TeamServiceImpl");

        teamRepository.save(team);
    }

    public List<Teammate> findTeammatesInTeam(String teamName) {
        log.debug("calling findTeammatesInTeam method in TeamServiceImpl");

        Team team = getByTeamname(teamName);

        return new ArrayList<>(team.getTeammates());
    }

    public Team addTeammateToTeam(String teamname, String teammateName) {
        log.debug("calling addTeammateToTeam method in TeamServiceImpl");

        Team existingTeam = getByTeamname(teamname);
        Teammate teammate = teammateService.findTeammateByName(teammateName);

//        Teammate tempTeammate = oTeammate.isEmpty()
//                ? teammateCreation(teammateName)
//                : oTeammate.get();

        existingTeam.getTeammates().add(teammate);
        return teamRepository.save(existingTeam);
    }


    public Team getTeamScore(Team team) {
        return null;
    }

    public void decreaseGamesPlayedByOne(Matches existingMatch) {
        log.debug("calling decreaseGamesPlayedByOne method in TeamServiceImpl");

        Team teamA = getByTeamname(existingMatch.getTeamA().getTeamName());
        Team teamB = getByTeamname(existingMatch.getTeamB().getTeamName());

        teamA.setTotalGamesPlayed(teamA.getTotalGamesPlayed() - 1);
        teamB.setTotalGamesPlayed(teamB.getTotalGamesPlayed() - 1);

        if (existingMatch.getWinner().equals(teamA.getTeamName())) {

            teamA.setWins(teamA.getWins() - 1);

        } else if (existingMatch.getWinner().equals(teamB.getTeamName())) {

            teamB.setWins(teamB.getWins() - 1);
        }

        initSave(teamA);
        initSave(teamB);
    }

    @Override
    public Team deleteTeammateFromTeam(String teamname, String teammate) {
        log.debug("calling deleteTeammateFromTeam method in TeamServiceImpl");

        Team existingTeam = getByTeamname(teamname);
        Teammate existingTeammate = teammateService.findTeammateByName(teammate);

        existingTeam.getTeammates().remove(existingTeammate);

        return save(existingTeam);
    }

    public Integer getScoresFromTeammates(String teamName) {
        log.debug("calling getScoresFromTeammates method in TeamServiceImpl");

        Team team = getByTeamname(teamName);

        return team.getTeammates().stream()
                .map(Teammate::getTotalGamesPlayed)
                .reduce(0, Integer::sum);
    }

    private Teammate teammateCreation(String teammateName) {
        log.debug("calling teammateCreation method in TeamServiceImpl");

        Teammate newTeammate = new Teammate();
        newTeammate.setName(teammateName);
        newTeammate.setTotalGamesPlayed(0);
        newTeammate.setWins(0);

        return teammateService.save(newTeammate);
    }
}
