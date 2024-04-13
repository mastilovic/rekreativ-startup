package com.example.rekreativ.service;

import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.repository.TeammateRepository;
import com.example.rekreativ.service.impl.TeammateServiceImpl;
import com.example.rekreativ.commons.CustomValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeammateServiceTest {

    @Mock
    private TeammateRepository teammateRepository;
    @Mock
    private CustomValidator customValidator;
    @Captor
    private ArgumentCaptor<Teammate> teammateArgumentCaptor;
    @InjectMocks
    private TeammateServiceImpl underTest;

    Teammate teammateOne, teammateTwo;

    @BeforeEach
    void setUp() {
        teammateOne = new Teammate();
        teammateOne.setId(0L);
        teammateOne.setName("teammate");
        teammateOne.setTotalGamesPlayed(2);
        teammateOne.setWins(2);
    }

    @Test
    void should_FindTeammateByName() {
        String teammateName = teammateOne.getName();

        when(teammateRepository.findTeammateByName(teammateName))
                .thenReturn(Optional.ofNullable(teammateOne));

        Teammate teammate = underTest.findTeammateByName(teammateName);

        assertThat(teammate).isEqualTo(teammateOne);
    }

    @Test
    void should_FindById() {
        Long id = teammateOne.getId();
        when(teammateRepository.findById(id))
                .thenReturn(Optional.ofNullable(teammateOne));

        Teammate teammate = underTest.findById(id);

        assertThat(teammate).isEqualTo(teammateOne);
    }

    @Test
    void should_Save() {
        underTest.save(teammateOne);

        then(teammateRepository).should().save(teammateArgumentCaptor.capture());
        Teammate capturedTeammate = teammateArgumentCaptor.getValue();

        assertThat(teammateOne.getName()).isEqualTo(capturedTeammate.getName());
    }

    @Test
    void should_InitSave() {
        when(teammateRepository.save(teammateOne))
                .thenReturn(teammateOne);

        Teammate teammate = underTest.initSave(teammateOne);

        assertThat(teammate).isEqualTo(teammateOne);
    }

    @Test
    void should_FindAll() {
        underTest.findAll();

        verify(teammateRepository).findAll();
    }

    @Test
    void should_Delete() {
        Long id = teammateOne.getId();
        when(teammateRepository.findById(id))
                .thenReturn(Optional.of(teammateOne));

        underTest.delete(id);

        verify(teammateRepository).deleteById(id);
        then(teammateRepository).should().deleteById(id);
    }
}