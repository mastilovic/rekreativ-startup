package com.example.RekreativStartup.Service;

import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.Teammate;
import com.example.RekreativStartup.model.User;
import com.example.RekreativStartup.repository.TeamRepository;
import com.example.RekreativStartup.repository.TeammateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeammateService {

    @Autowired
    private TeammateRepository teammateRepository;
    @Autowired
    private TeamRepository teamRepository;

    public Optional<Teammate> findTeammateByName(String name){
        return teammateRepository.findTeammateByName(name);
    }

//    public Optional<Teammate> getTeammateByTeamName(String teamName){
//        return teammateRepository.findTeammateByTeamName(teamName);
//    }

//    public Optional<Team> getAllTeammatesByTeamName(String teamName){
//        return teamRepository.findAllTeammatesByTeamName(teamName);
//    }

    public Teammate save(Teammate teammate) {
//        team.setUser(team.getUser());
//        team.setScore(null);
        return teammateRepository.save(teammate);
    }

    public Iterable<Teammate> findAll(){
        return teammateRepository.findAll();
    }

    public void delete(Long id) {
        teammateRepository.deleteById(id);
    }
}
