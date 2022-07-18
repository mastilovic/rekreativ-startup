package com.example.RekreativStartup.service;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.RekreativStartup.model.*;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.repository.TeammateRepository;
import com.example.RekreativStartup.repository.UserRepository;
import com.example.RekreativStartup.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
public class TeamService {

    private final JwtUtil jwtUtil;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeammateRepository teammateRepository;
    private final TeammateService teammateService;

    @Autowired
    public TeamService(JwtUtil jwtUtil,
                       TeamRepository teamRepository,
                       UserRepository userRepository,
                       TeammateRepository teammateRepository,
                       TeammateService teammateService) {
        this.jwtUtil = jwtUtil;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teammateRepository = teammateRepository;
        this.teammateService = teammateService;
    }



    public Iterable<Team> findAll(){
        return teamRepository.findAll();
    }

    public Optional<Team> findTeamById(Long id){
        return teamRepository.findById(id);
    }

    public Optional<Team> getByTeamname(String teamName){
        return teamRepository.findByTeamName(teamName);
    }

    public void delete(Long id) {
        teamRepository.deleteById(id);
    }

    public Team saveTeamWithUsername(Team team, HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = jwtUtil.decodedToken(token);
        String username = decodedJWT.getSubject();

        User newUser = new User();
        newUser.setUsername(username);
        return teamRepository.save(team);
    }

    public Team save(Team team) {
        return teamRepository.save(team);
    }

    public ArrayList<Teammate> findTeammatesInTeam(String teamName){
        Optional<Team> existingTeam = getByTeamname(teamName);
        if (existingTeam == null){
            return null;
        }
        ArrayList<Teammate> teammatesInTeam = new ArrayList<Teammate>();

        existingTeam.map(Team::getTeammates).get().forEach(teammate ->
                teammatesInTeam.add(teammate));
        return teammatesInTeam;
    }

    public void addTeammateToTeam(String teamname, String teammateName){
        Team existingTeam = teamRepository.findByTeamName(teamname).get();
        Optional<Teammate> optionalTeammate = teammateRepository.findTeammateByName(teammateName);

        if(!optionalTeammate.isPresent()){
            Teammate newTeammate = new Teammate();
            newTeammate.setName(teammateName);
            newTeammate.setTotalGamesPlayed(0);
            newTeammate.setWins(0);
            teammateService.save(newTeammate);
            existingTeam.getTeammates().add(newTeammate);
            save(existingTeam);
        } else {
            Teammate existingTeammate = optionalTeammate.get();
            existingTeam.getTeammates().add(existingTeammate);
            save(existingTeam);
        }
    }

    public Team getTeamScore(Team team){
        return null;
    }

    public void decreaseGamesPlayedByOne(Matches existingMatch){
        Team teamA = getByTeamname(existingMatch.getTeamA().getTeamName()).get();
        Team teamB = getByTeamname(existingMatch.getTeamB().getTeamName()).get();
        teamA.setTotalGamesPlayed(teamA.getTotalGamesPlayed() - 1);
        teamB.setTotalGamesPlayed(teamB.getTotalGamesPlayed() - 1);
        if (Objects.equals(existingMatch.getWinner(), teamA.getTeamName())){
            teamA.setWins(teamA.getWins() - 1);
        } else if(Objects.equals(existingMatch.getWinner(), teamB.getTeamName())){
            teamB.setWins(teamB.getWins() - 1);
        }
        save(teamA);
        save(teamB);
    }
}
