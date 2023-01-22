package com.example.rekreativ.service;

import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Role;
import com.example.rekreativ.repository.RoleRepository;
import com.example.rekreativ.service.impl.RoleServiceImpl;
import com.example.rekreativ.util.ValidatorUtil;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {


    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ValidatorUtil validatorUtil;
    @InjectMocks
    private RoleServiceImpl underTest;
    @Captor
    private ArgumentCaptor<Role> roleArgumentCaptor;

    Role roleUser, roleAdmin;

    @BeforeEach
    void setUp() {
        roleUser = new Role("ROLE_USER");
        roleAdmin = new Role("ROLE_ADMIN");
    }
    @Test
    void should_FindByName() {
        String roleUserName = roleUser.getName();
        when(roleRepository.findByName(roleUserName))
                .thenReturn(Optional.of(roleUser));

        Role role = underTest.findByName(roleUserName);

        assertThat(role).isEqualTo(roleUser);
    }

    @Test
    void should_ThrowIfNotFoundByName() {
        String roleUserName = roleUser.getName();
        when(roleRepository.findByName(roleUserName))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findByName(roleUserName))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void should_Save() {
        String roleUserName = roleUser.getName();
        when(roleRepository.findByName(roleUserName))
                .thenReturn(Optional.empty());

        underTest.save(roleUser);
        then(roleRepository).should().save(roleArgumentCaptor.capture());
        Role capturedRole = roleArgumentCaptor.getValue();

        assertThat(roleUser.getName()).isEqualTo(capturedRole.getName());
    }

    @Test
    void should_ThrowIfRoleNotFoundForSave() {
        String roleUserName = roleUser.getName();
        when(roleRepository.findByName(roleUserName))
                .thenReturn(Optional.of(roleUser));

        assertThatThrownBy(() -> underTest.save(roleUser))
                .isInstanceOf(ObjectAlreadyExistsException.class);
    }

    @Test
    void should_InitSave() {
        when(roleRepository.save(roleUser))
                .thenReturn(roleUser);

        Role role = underTest.initSave(roleUser);

        assertThat(role).isEqualTo(roleUser);
    }

    @Test
    void should_ExistsByName() {
        String roleUserName = roleUser.getName();
        when(roleRepository.findByName(roleUserName))
                .thenReturn(Optional.of(roleUser));

        underTest.existsByName(roleUserName);
        verify(roleRepository).findByName(roleUserName);
    }

    @Test
    void should_FindAll() {
        underTest.findAll();

        verify(roleRepository).findAll();
    }
}