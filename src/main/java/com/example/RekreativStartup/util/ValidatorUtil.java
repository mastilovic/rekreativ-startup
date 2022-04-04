package com.example.RekreativStartup.util;

import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.repository.TeamRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class ValidatorUtil {

    private final TeamRepository teamRepository;

    @Autowired
    public ValidatorUtil(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public static boolean teamValidator(Team team){
        return !StringUtils.isBlank(String.valueOf(team));
    }

    public Integer getScoresFromTeammates(String teamName){
        Optional<Team> optionalTeam = teamRepository.findByTeamName(teamName);
        ArrayList<Integer> teamScoreList = new ArrayList<Integer>();

        optionalTeam.map(Team::getTeammates).get().forEach(teammate ->
                teamScoreList.add(teammate.getPersonalScore()));

        Integer teamScore = 0;
        for(int i = 0; i < teamScoreList.size();i++){
            teamScore+=teamScoreList.get(i);
        }
        return teamScore;
    }
}
