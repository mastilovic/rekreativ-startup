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
import com.example.rekreativ.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ValidatorUtil validatorUtil;

    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder,
                           ValidatorUtil validatorUtil) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Username '%s' was not found",username)));

        return new AuthUser(user);
    }

    public void addRoleToUser(String username, String roles){

        if (!roleService.existsByName(roles)){

            throw new ObjectNotFoundException(Role.class, roles);
        }

        User user = findUserByUsername(username);

        boolean userContainsRole = user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(r-> r.equals(roles));

        if (userContainsRole) {
            log.debug("User already has {} role!", roles);

            throw new ObjectAlreadyExistsException(Role.class, "User already has that role!");
        }

        Role role = roleService.findByName(roles);
        user.getRoles().add(role);
    }

    public User saveUser(User user) {
        log.info("Saving new user {} to the database", user.getUsername());

        if(userRepository.findByUsername(user.getUsername()).isPresent()){
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

        validatorUtil.validate(newUser);

        return userRepository.save(newUser);
    }

    @Override
    public User initSave(User user) {

        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        User user = findUserById(id);

        userRepository.deleteById(user.getId());
    }

    public void delete(User user) {
        User existingUser = findUserById(user.getId());

        userRepository.delete(existingUser);
    }

    public Iterable<UserDTO> findAll(){
        Iterable<User> users = userRepository.findAll();

        ModelMapper modelMapper = new ModelMapper();

        return StreamSupport.stream(users.spliterator(), false)
                .map(u -> modelMapper.map(u, UserDTO.class))
                .collect(Collectors.toList());
    }

    public Page<User> findAllPageable(Pageable pageable) {

        return userRepository.findAll(pageable);
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            log.debug("User not found with id: {}", id);

            throw new ObjectNotFoundException(User.class, id);
        }

        return user.get();
    }

    @Override
    public boolean existsById(Long id) {

        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {

        return userRepository.findByUsername(username).isPresent();
    }

    public User findUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()){
            log.debug("user not found with username: {}", username);

            throw new ObjectNotFoundException(User.class, username);
        }

        return user.get();
    }

}
