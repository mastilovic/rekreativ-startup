package com.example.rekreativ.service;

import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.repository.TeamRepository;
import com.example.rekreativ.service.impl.TeamServiceImpl;
import com.example.rekreativ.commons.JwtHandler;
import com.example.rekreativ.commons.CustomValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private JwtHandler jwtHandler;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private TeammateService teammateService;
    @Mock
    private CustomValidator customValidator;
    @InjectMocks
    private TeamServiceImpl underTest;
    @Captor
    private ArgumentCaptor<Team> teamArgumentCaptor;

    Team teamOne, teamTwo;
    Teammate teammateOne;

    @BeforeEach
    void setUp() {
        teamOne = new Team(0L,
                "teamOneName",
                "teamOneCity",
                3,
                3);

        teamTwo = new Team(1L,
                "teamTwoName",
                "teamTwoCity",
                1,
                3);

        teammateOne = new Teammate(0L,
                "teammateOne",
                2,
                1,
                50.00);
    }

    @Test
    void should_FindAll() {
        underTest.findAll();

        verify(teamRepository).findAll();
    }

    @Test
    void should_FindTeamById() {
        Long teamId = teamOne.getId();
        when(teamRepository.findById(teamId))
                .thenReturn(Optional.of(teamOne));

        Team team = underTest.findTeamById(teamId);

        assertThat(team).isEqualTo(teamOne);
    }

    @Test
    void should_ThrowIfTeamNotFoundById() {
        Long teamId = teamOne.getId();
        when(teamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findTeamById(teamId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void should_GetByTeamname() {
        String teamName = teamOne.getTeamName();
        when(teamRepository.findByTeamName(teamName))
                .thenReturn(Optional.of(teamOne));

        Team team = underTest.getByTeamname(teamName);

        assertThat(team).isEqualTo(teamOne);
    }

    @Test
    void should_ThrowIfTeamNotFoundByTeamname() {
        String teamName = teamOne.getTeamName();
        when(teamRepository.findByTeamName(teamName))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getByTeamname(teamName))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void should_Delete() {
        Long id = teamOne.getId();
        when(teamRepository.findById(id))
                .thenReturn(Optional.of(teamOne));

        underTest.delete(id);
        verify(teamRepository).deleteById(id);
        then(teamRepository).should().deleteById(id);
    }

    @Test
    @Disabled
    void should_SaveTeamWithUsername() {
    }

    @Test
    void should_Save() {
        String teamName = teamOne.getTeamName();
        when(teamRepository.findByTeamName(teamName))
                .thenReturn(Optional.empty());
        when(teamRepository.save(teamOne))
                .thenReturn(teamOne);

        Team team = underTest.save(teamOne);

        assertThat(team.getTeamName()).isEqualTo(teamName);
    }

    @Test
    void should_InitSave() {
        underTest.initSave(teamOne);

        verify(teamRepository).save(teamOne);
    }

    @Test
    void should_FindTeammatesInTeam() {
        String teamName = teamOne.getTeamName();
        when(teamRepository.findByTeamName(teamName))
                .thenReturn(Optional.of(teamOne));

        List<Teammate> teammatesInTeam = underTest.findTeammatesInTeam(teamName);

        assertThat(teammatesInTeam).isEqualTo(teamOne.getTeammates());
    }

    @Test
    void should_AddTeammateToTeam() {
        String teammateName = teammateOne.getName();
        String teamName = teamOne.getTeamName();
        when(teamRepository.findByTeamName(teamName))
                .thenReturn(Optional.of(teamOne));
        when(teammateService.findTeammateByName(teammateName))
                .thenReturn(teammateOne);
        when(teamRepository.save(teamOne))
                .thenReturn(teamOne);

        Team team = underTest.addTeammateToTeam(teamName, teammateName);
        Collection<Teammate> teammates = team.getTeammates();

        assertThat(teammateOne).isIn(teammates);
    }

    @Test
    @Disabled
    void should_GetTeamScore() {
    }

    @Test
    @Disabled
    void should_DecreaseGamesPlayedByOne() {
    }

    @Test
    @Disabled
    void should_DeleteTeammateFromTeam() {
    }

    @Test
    @Disabled
    void should_GetScoresFromTeammates() {
    }
}