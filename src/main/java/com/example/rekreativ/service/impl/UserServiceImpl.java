package com.example.rekreativ.service.impl;

import com.example.rekreativ.auth.AuthUser;
import com.example.rekreativ.dto.UserDTO;
import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.model.Role;
import com.example.rekreativ.model.User;
import com.example.rekreativ.repository.UserRepository;
import com.example.rekreativ.service.RoleService;
import com.example.rekreativ.service.UserService;
import com.example.rekreativ.commons.CustomValidator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CustomValidator customValidator;

    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder,
                           CustomValidator customValidator) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.customValidator = customValidator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("calling loadUserByUsername method");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username '%s' was not found", username)));

        return new AuthUser(user);
    }

    public void addRoleToUser(String username, String roleName) {
        log.debug("calling addRoleToUser method");

        User user = findUserByUsername(username);
        Role role = roleService.findByName(roleName);

        boolean userContainsRole = user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(r -> r.equals(roleName));

        if (userContainsRole) {
            log.debug("User already has {} role!", roleName);

            throw new ObjectAlreadyExistsException(Role.class, "User already has role: " + roleName);
        }

        user.getRoles().add(role);
    }

    public User saveUser(User user) {
        log.debug("calling saveUser method");

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            log.debug("user already exists with username: {}", user.getUsername());

            throw new ObjectAlreadyExistsException(User.class, user.getUsername());
        }

        Role role = roleService.findByName("ROLE_USER");
        Role roleAdmin = roleService.findByName("ROLE_ADMIN");

        User newUser = new User();
        newUser.getRoles().add(role);
        newUser.getRoles().add(roleAdmin);
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        customValidator.validate(newUser);

        return userRepository.save(newUser);
    }

    @Override
    public User initSave(User user) {
        log.debug("calling initSave method");

        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        log.debug("calling deleteUserById method");

        User user = findUserById(id);

        userRepository.deleteById(user.getId());
    }

    public void delete(User user) {
        log.debug("calling delete method");

        User existingUser = findUserById(user.getId());

        userRepository.delete(existingUser);
    }

    public Iterable<UserDTO> findAll() {
        log.debug("calling findAll method");

        Iterable<User> users = userRepository.findAll();

        ModelMapper modelMapper = new ModelMapper();

        return StreamSupport.stream(users.spliterator(), false)
                .map(u -> modelMapper.map(u, UserDTO.class))
                .collect(Collectors.toList());
    }

    public Page<User> findAllPageable(Pageable pageable) {
        log.debug("calling findAllPageable method");

        return userRepository.findAll(pageable);
    }

    public User findUserById(Long id) {
        log.debug("calling findUserById method");

        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(User.class, id));
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("calling existsById method");

        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        log.debug("calling existsByUsername method");

        return userRepository.findByUsername(username).isPresent();
    }

    public User findUserByUsername(String username) {
        log.debug("calling findUserByUsername method");

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(User.class, username));
    }
}
