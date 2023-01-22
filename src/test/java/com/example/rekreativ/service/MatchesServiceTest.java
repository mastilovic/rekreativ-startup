package com.example.rekreativ.service;

import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.repository.MatchesRepository;
import com.example.rekreativ.service.impl.MatchesServiceImpl;
import com.example.rekreativ.service.impl.TeamServiceImpl;
import com.example.rekreativ.service.impl.TeammateServiceImpl;
import com.example.rekreativ.util.ValidatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.will;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchesServiceTest {

    @Mock
    private MatchesRepository matchesRepository;
    @Mock
    private TeammateServiceImpl teammateService;
    @Mock
    private TeamServiceImpl teamService;
    @Mock
    private ValidatorUtil validatorUtil;
    @InjectMocks
    private MatchesServiceImpl underTest;

    Matches matchOne, emptyMatch;
    Team teamOne, teamTwo;

    @BeforeEach
    void setUp() {
        teamOne = new Team(0L,
                "teamOneName",
                "teamOneCity",
                1,
                1);

        teamTwo = new Team(1L,
                "teamTwoName",
                "teamTwoCity",
                1,
                1);

        matchOne = new Matches(0L,
                teamOne,
                teamTwo);

    }

    @Test
    void should_CreateMatchup() {
        String teamOneName = teamOne.getTeamName();
        String teamTwoName = teamTwo.getTeamName();

        when(teamService.getByTeamname(teamOneName))
                .thenReturn(teamOne);
        when(teamService.getByTeamname(teamTwoName))
                .thenReturn(teamTwo);

        underTest.createMatchup(teamOneName, teamTwoName, 1, 1);

        verify(matchesRepository).save(matchOne);
    }

    @Test
    void should_Save() {
    }

    @Test
    void should_MatchOutcome() {
    }

    @Test
    void should_FindAll() {
    }

    @Test
    void should_FindMatchById() {
    }

    @Test
    void should_Delete() {
    }
}