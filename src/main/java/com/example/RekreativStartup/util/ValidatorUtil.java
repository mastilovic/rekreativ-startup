package com.example.RekreativStartup.util;

import com.example.RekreativStartup.model.Role;
import com.example.RekreativStartup.model.Team;
import com.example.RekreativStartup.model.Teammate;
import com.example.RekreativStartup.model.User;
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

    public Integer getScoresFromTeammates(String teamName){
        Optional<Team> optionalTeam = teamRepository.findByTeamName(teamName);
        ArrayList<Integer> teamScoreList = new ArrayList<Integer>();

        optionalTeam.map(Team::getTeammates).get().forEach(teammate ->
                teamScoreList.add(teammate.getTotalGamesPlayed()));

        Integer teamScore = 0;
        for(int i = 0; i < teamScoreList.size();i++){
            teamScore+=teamScoreList.get(i);
        }
        return teamScore;
    }

    public static boolean userValidator(User user){
        return StringUtils.isBlank(user.getUsername()) ||
                StringUtils.isBlank(user.getPassword()) ||
                StringUtils.containsWhitespace(user.getPassword());
    }

    public static boolean teammateValidator(Teammate teammate){
        return StringUtils.isBlank(teammate.getName());
    }

    public static boolean teamValidator(Team team){
        return StringUtils.isBlank(team.getTeamName());
    }

    public static boolean roleValidator(Role role){
        return StringUtils.isBlank(role.getName());
    }
}
