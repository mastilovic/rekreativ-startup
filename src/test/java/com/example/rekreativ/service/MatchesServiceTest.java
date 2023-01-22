package com.example.rekreativ.service;

import com.example.rekreativ.dto.MatchesRequestDTO;
import com.example.rekreativ.model.Matches;
import com.example.rekreativ.model.Role;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.repository.MatchesRepository;
import com.example.rekreativ.service.impl.MatchesServiceImpl;
import com.example.rekreativ.service.impl.TeamServiceImpl;
import com.example.rekreativ.service.impl.TeammateServiceImpl;
import com.example.rekreativ.util.ValidatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
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
    @Captor
    private ArgumentCaptor<Matches> matchesArgumentCaptor;

    Matches matchOne, emptyMatch;
    MatchesRequestDTO matchesRequestDTO;
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

        matchesRequestDTO = new MatchesRequestDTO(
                teamOne.getTeamName(),
                teamTwo.getTeamName(),
                1,
                2);
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
        then(matchesRepository).should().save(matchesArgumentCaptor.capture());
        Matches matchesArgumentCaptorValue = matchesArgumentCaptor.getValue();

        assertThat(matchesArgumentCaptorValue.getTeamA()).isEqualTo(teamOne);
        assertThat(matchesArgumentCaptorValue.getTeamB()).isEqualTo(teamTwo);
    }

    @Test
    void should_Save() {
        String teamOneName = teamOne.getTeamName();
        String teamTwoName = teamTwo.getTeamName();

        when(teamService.getByTeamname(teamOneName))
                .thenReturn(teamOne);
        when(teamService.getByTeamname(teamTwoName))
                .thenReturn(teamTwo);

        underTest.save(matchesRequestDTO);
        then(matchesRepository).should().save(matchesArgumentCaptor.capture());
        Matches matchesArgumentCaptorValue = matchesArgumentCaptor.getValue();

        assertThat(teamOne).isEqualTo(matchesArgumentCaptorValue.getTeamA());
        assertThat(teamTwo).isEqualTo(matchesArgumentCaptorValue.getTeamB());
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