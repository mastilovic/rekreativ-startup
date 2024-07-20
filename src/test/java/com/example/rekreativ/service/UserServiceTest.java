package com.example.rekreativ.service;

import com.example.rekreativ.auth.AuthUser;
import com.example.rekreativ.commons.CustomValidator;
import com.example.rekreativ.model.dto.UserLoginDTO;
import com.example.rekreativ.model.dto.response.UserResponseDTO;
import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.mapper.UserMapper;
import com.example.rekreativ.model.Role;
import com.example.rekreativ.model.User;
import com.example.rekreativ.repository.RoleRepository;
import com.example.rekreativ.repository.UserRepository;
import com.example.rekreativ.service.impl.UserServiceImpl;
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

import java.util.Collections;
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
    private CustomValidator customValidator;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl underTest;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    User userOne, emptyUser;
    UserResponseDTO userOneDto;
    Role roleUser, roleAdmin;
    AuthUser authUser;

    @BeforeEach
    void setUp() {
        userOne = new User(0L,
                "testUsername",
                "testPassword");
        userOneDto = new UserResponseDTO(0L,
                                         "testUsername",
                                         Collections.emptyList(),
                                         0.0,
                                         0,
                                         Collections.emptySet(),
                                         Collections.emptyList(),
                                         Collections.emptyList());

        emptyUser = new User(null,
                " ",
                " ");

        authUser = new AuthUser(userOne);

        roleUser = new Role("ROLE_USER");
        roleAdmin = new Role("ROLE_ADMIN");

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

        UserResponseDTO user = underTest.findUserByUsername(userOne.getUsername());

        assertThat(user.getUsername()).isEqualTo(userOne.getUsername());
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
        when(roleService.findByName(roleUser.getName()))
                .thenReturn(roleUser);

        underTest.addRoleToUser(userOne.getUsername(), roleUser.getName());

        assertThat(userOne.getRoles()).contains(roleUser);
    }

    @Test
    void should_ThrowIfUserAlreadyHasThatRole() {
        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.of(userOne));
        when(roleService.findByName(roleUser.getName()))
                .thenReturn(roleUser);

        userOne.getRoles().add(roleUser);
        assertThatThrownBy(() -> underTest.addRoleToUser(userOne.getUsername(), roleUser.getName()))
                .isInstanceOf(ObjectAlreadyExistsException.class);
    }

    @Test
    void should_saveUser() {
        long userId = 1L;
        userOne.setId(userId);

        UserLoginDTO newUser = new UserLoginDTO();
//        newUser.setId(userOne.getId());
        newUser.setUsername(userOne.getUsername());
        newUser.setPassword(passwordEncoder.encode(userOne.getPassword()));
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userOne.getId());
        userResponseDTO.setUsername(userOne.getUsername());

        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.empty());
        when(roleService.findByName(anyString()))
                .thenReturn(null);

        underTest.saveUser(newUser);

        then(userRepository).should().save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(newUser.getUsername()).isEqualTo(capturedUser.getUsername());
    }

    @Test
    void should_ThrowIfUserIsPresentForSaveUser() {
        long userId = 1L;
        userOne.setId(userId);
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(userOne.getUsername());
        userLoginDTO.setPassword(userOne.getPassword());
        when(userRepository.findByUsername(userOne.getUsername()))
                .thenReturn(Optional.of(userOne));

        assertThatThrownBy(() -> underTest.saveUser(userLoginDTO))
                .isInstanceOf(ObjectAlreadyExistsException.class);
    }

    @Test
    void should_initSave() {
        when(userRepository.save(userOne))
                .thenReturn(userOne);

        UserResponseDTO userResponseDTO = underTest.initSave(userOne);

        assertThat(userResponseDTO.getUsername()).isEqualTo(userOne.getUsername());
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

        underTest.deleteUserById(userOne.getId());
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
        UserResponseDTO user = underTest.findUserById(userOne.getId());

        assertThat(user.getUsername()).isEqualTo(userOne.getUsername());
    }

    @Test
    void should_ThrowIfUserNotFoundForFindUserById() {
        when(userRepository.findById(userOne.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findUserById(userOne.getId()))
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