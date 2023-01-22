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
import com.example.rekreativ.util.JwtUtil;
import com.example.rekreativ.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    private final JwtUtil jwtUtil;
    private final TeamRepository teamRepository;
    private final TeammateService teammateService;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public TeamServiceImpl(JwtUtil jwtUtil,
                           TeamRepository teamRepository,
                           TeammateService teammateService,
                           ValidatorUtil validatorUtil) {
        this.jwtUtil = jwtUtil;
        this.teamRepository = teamRepository;
        this.teammateService = teammateService;
        this.validatorUtil = validatorUtil;
    }

    public Iterable<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team findTeamById(Long id) {
        Optional<Team> team = teamRepository.findById(id);

        if (team.isEmpty()) {
            log.debug("Team with id: {} doesnt exist!", id);

            throw new ObjectNotFoundException(Team.class, id);
        }

        return team.get();
    }

    public Team getByTeamname(String teamName) {
        log.debug("calling getByTeamname method in TeamServiceImpl");

        Optional<Team> team = teamRepository.findByTeamName(teamName);

        if (team.isEmpty()) {
            log.debug("Team not found with name: {}", teamName);

            throw new ObjectNotFoundException(Team.class, teamName);
        }

        return team.get();
    }

    public void delete(Long id) {
        Team team = findTeamById(id);

        teamRepository.deleteById(team.getId());
    }

    public Team saveTeamWithUsername(Team team, HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = jwtUtil.decodedToken(token);
        String username = decodedJWT.getSubject();

        User user = new User();
        user.setUsername(username);
        validatorUtil.validate(user);

        return teamRepository.save(team);
    }

    public Team save(Team team) {
        log.info("inside save method in TeamServiceImpl");
        Optional<Team> optionalTeam = teamRepository.findByTeamName(team.getTeamName());

        if(optionalTeam.isPresent()) {
            log.error("Team already exists with name: " + team.getTeamName());

            throw new ObjectAlreadyExistsException(Team.class, team.getTeamName());
        }

        Team newTeam = new Team();
        newTeam.setTeamName(team.getTeamName());
        newTeam.setCity(team.getCity());
        newTeam.setWins(0);
        newTeam.setTotalGamesPlayed(0);
        validatorUtil.validate(newTeam);

        return teamRepository.save(team);
    }

    public void initSave(Team team) {

        teamRepository.save(team);
    }

    public List<Teammate> findTeammatesInTeam(String teamName) {
        Team team = getByTeamname(teamName);

        return team.getTeammates().stream().collect(Collectors.toList());
    }

    public Team addTeammateToTeam(String teamname, String teammateName) {
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
        Team existingTeam = getByTeamname(teamname);
        Teammate existingTeammate = teammateService.findTeammateByName(teammate);

        if (existingTeam == null) {

            throw new ObjectNotFoundException(Team.class, "Team not found!");
        }

        existingTeam.getTeammates().remove(existingTeammate);
        return save(existingTeam);
    }

    public Integer getScoresFromTeammates(String teamName) {

        Team team = getByTeamname(teamName);

        List<Integer> teamScoreList = team.getTeammates().stream()
                .map(Teammate::getTotalGamesPlayed)
                .collect(Collectors.toList());

        Integer teamScore = 0;

        for (int i = 0; i < teamScoreList.size(); i++) {
            teamScore += teamScoreList.get(i);
        }

        return teamScore;
    }

    private Teammate teammateCreation(String teammateName) {

        Teammate newTeammate = new Teammate();
        newTeammate.setName(teammateName);
        newTeammate.setTotalGamesPlayed(0);
        newTeammate.setWins(0);

        return teammateService.save(newTeammate);
    }
}
