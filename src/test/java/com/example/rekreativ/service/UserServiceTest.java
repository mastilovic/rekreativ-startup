package com.example.rekreativ.service;

import com.example.rekreativ.auth.AuthUser;
import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Role;
import com.example.rekreativ.model.User;
import com.example.rekreativ.repository.RoleRepository;
import com.example.rekreativ.repository.UserRepository;
import com.example.rekreativ.service.impl.UserServiceImpl;
import com.example.rekreativ.util.ValidatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private ValidatorUtil validatorUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl underTest;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    User userOne, emptyUser;
    Role userRole, adminRole;
    AuthUser authUser;

    @BeforeEach
    void setUp() {
        userOne = new User(0L,
                "testUsername",
                "testPassword");

        emptyUser = new User(null,
                " ",
                " ");

        authUser = new AuthUser(userOne);

        userRole = new Role("ROLE_USER");
        adminRole = new Role("ROLE_ADMIN");

    }

    @Test
    void should_findAll() {
        underTest.findAll();

        verify(userRepository).findAll();
    }

    @Test
    void should_loadUserByUsername() {
        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.ofNullable(userOne));

        UserDetails user = underTest.loadUserByUsername(userOne.getUsername());

        assertThat(user.getUsername()).isEqualTo(authUser.getUsername());
    }

    @Test
    void should_ThrowIfLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.loadUserByUsername(userOne.getUsername()))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void should_findUserByUsername() {
        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.of(userOne));

        User user = underTest.findUserByUsername(userOne.getUsername());

        assertThat(user).isEqualTo(userOne);
    }

    @Test
    void should_ThrowIfFindUserByUsernameNotFound() {
        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findUserByUsername(userOne.getUsername()))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void should_addRoleToUser() {
        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.of(userOne));
        when(roleService.findByName(userRole.getName()))
                .thenReturn(userRole);

        underTest.addRoleToUser(userOne.getUsername(), userRole.getName());

        assertThat(userOne.getRoles()).contains(userRole);
    }

    @Test
    void should_ThrowIfUserAlreadyHasThatRole() {
        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.of(userOne));
        when(roleService.findByName(userRole.getName()))
                .thenReturn(userRole);

        userOne.getRoles().add(userRole);
        assertThatThrownBy(() ->  underTest.addRoleToUser(userOne.getUsername(), userRole.getName()))
                .isInstanceOf(ObjectAlreadyExistsException.class);
    }

    @Test
    void should_saveUser() {
        long userId = 1L;
        userOne.setId(userId);
        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.empty());

        User newUser = new User();
        newUser.setId(userOne.getId());
        newUser.setUsername(userOne.getUsername());
        newUser.setPassword(passwordEncoder.encode(userOne.getPassword()));

        underTest.saveUser(newUser);
//        verify(userRepository).save(userArgumentCaptor.capture());
        then(userRepository).should().save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(newUser.getUsername()).isEqualTo(capturedUser.getUsername());
    }

    @Test
    void should_ThrowIfUserIsPresentForSaveUser() {
        long userId = 1L;
        userOne.setId(userId);
        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.of(userOne));

        assertThatThrownBy(() ->  underTest.saveUser(userOne))
                .isInstanceOf(ObjectAlreadyExistsException.class);
    }

    @Test
    void should_initSave() {
        when(userRepository.save(userOne))
                .thenReturn(userOne);

        User user = underTest.initSave(userOne);

        assertThat(user).isEqualTo(userOne);
    }

    @Test
    void should_deleteUserById() {
        Long id = userOne.getId();
        when(userRepository.findById(id))
                .thenReturn(Optional.of(userOne));

        underTest.deleteUserById(id);
        verify(userRepository).deleteById(id);
        then(userRepository).should().deleteById(id);
    }

    @Test
    void should_delete() {
        Long id = userOne.getId();
        when(userRepository.findById(id))
                .thenReturn(Optional.of(userOne));

        underTest.delete(userOne);
        verify(userRepository).delete(userOne);
        then(userRepository).should().delete(userOne);
    }

    @Test
    @Disabled
    void should_findAllPageable() {
//        underTest.findAllPageable();
//        verify(userRepository).findAll();
    }

    @Test
    void should_findUserById() {
        when(userRepository.findById(userOne.getId()))
                .thenReturn(Optional.of(userOne));
        User user = underTest.findUserById(userOne.getId());

        assertThat(user).isEqualTo(userOne);
    }

    @Test
    void should_ThrowIfUserNotFoundForFindUserById() {
        when(userRepository.findById(userOne.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->  underTest.findUserById(userOne.getId()))
                .isInstanceOf(ObjectNotFoundException.class);
    }


    @Test
    void existsById() {
        Long id = userOne.getId();
        when(userRepository.existsById(id))
                .thenReturn(true);

        underTest.existsById(id);
        verify(userRepository).existsById(id);
    }

    @Test
    void existsByUsername() {
        String username = userOne.getUsername();
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(userOne));

        underTest.existsByUsername(username);
        verify(userRepository).findByUsername(username);
    }
}