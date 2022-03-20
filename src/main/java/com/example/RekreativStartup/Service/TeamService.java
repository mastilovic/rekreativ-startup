package com.example.RekreativStartup.Service;


import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Iterable<Team> findAll(){
        return teamRepository.findAll();
    }

    public Optional<Team> findById(Long id){
        return teamRepository.findById(id);
    }

    public Optional<Team> findByTeamname(String teamName){
        return teamRepository.findByTeamName(teamName);
    }

    public Team save(Team team) {
        return teamRepository.save(team);
    }
}
