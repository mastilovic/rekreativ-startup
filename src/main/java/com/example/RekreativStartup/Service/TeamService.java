package com.example.RekreativStartup.Service;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.RekreativStartup.model.Role;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.User;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.repository.UserRepository;
import com.example.RekreativStartup.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class TeamService {

    private final JwtUtil jwtUtil;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamService(JwtUtil jwtUtil, TeamRepository teamRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }



    public Iterable<Team> findAll(){
        return teamRepository.findAll();
    }

    public Optional<Team> findById(Long id){
        return teamRepository.findById(id);
    }

    public Optional<Team> getByTeamname(String teamName){
        return teamRepository.findByTeamName(teamName);
    }

    public Optional<Team> findByTeammate(String teammate){
        return teamRepository.findByTeammate(teammate);
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




        team.setScore(null);
        return teamRepository.save(team);
    }

    public Team save(Team team) {
//        team.setUser(team.getUser());
        team.setScore(null);
        return teamRepository.save(team);
    }

    public void addTeammateToTeam(String teamname, String username){
        Team existingTeam = teamRepository.findByTeamName(teamname).get();
        User existingUser = userRepository.findByUsername(username).get();
        existingUser.getTeam().add(existingTeam.getTeamName());
    }
}
