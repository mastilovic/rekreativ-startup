package com.example.RekreativStartup.Service;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.RekreativStartup.model.Role;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.Teammate;
import com.example.RekreativStartup.model.User;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.repository.TeammateRepository;
import com.example.RekreativStartup.repository.UserRepository;
import com.example.RekreativStartup.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;
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

    public Optional<Team> findByTeammate(String teammate){
        return teamRepository.findByTeammate(teammate);
    }

    public void delete(Long id) {
        teamRepository.deleteById(id);
    }

    public void deleteTeammate(String teammate){
        teamRepository.deleteTeammate(teammate);
    }

    public Team saveTeamWithUsername(Team team, HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = jwtUtil.decodedToken(token);
        String username = decodedJWT.getSubject();

        User newUser = new User();
        newUser.setUsername(username);

//        newUser.getUsername().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        });
//        team.setScore(null);
        return teamRepository.save(team);
    }

    public Team save(Team team) {
//        team.setUser(team.getUser());
//        team.setScore(null);
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

    public void addTeammateToTeam(String teamname, String username){
        Team existingTeam = teamRepository.findByTeamName(teamname).get();
        Optional<Teammate> optionalTeammate = teammateRepository.findTeammateByName(username);

        if(!optionalTeammate.isPresent()){
            Teammate newTeammate = new Teammate();
            newTeammate.setName(username);
            newTeammate.setPersonalScore(null);
            teammateService.save(newTeammate);
            existingTeam.getTeammates().add(newTeammate);
            teamRepository.save(existingTeam);
        } else {
            Teammate existingTeammate = optionalTeammate.get();
            existingTeam.getTeammates().add(existingTeammate);
            teamRepository.save(existingTeam);
        }
    }

    public Team getTeamScore(Team team){
        return null;
    }
}
