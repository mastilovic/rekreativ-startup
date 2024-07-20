package com.example.rekreativ.service.impl;

import com.example.rekreativ.auth.AuthUser;
import com.example.rekreativ.commons.CustomValidator;
import com.example.rekreativ.model.Player;
import com.example.rekreativ.model.dto.UserLoginDTO;
import com.example.rekreativ.model.dto.request.ReviewRequestDto;
import com.example.rekreativ.model.dto.request.UserPlayerTypeRequestDto;
import com.example.rekreativ.model.dto.response.UserResponseDTO;
import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.mapper.ReviewMapper;
import com.example.rekreativ.mapper.UserMapper;
import com.example.rekreativ.model.Review;
import com.example.rekreativ.model.Role;
import com.example.rekreativ.model.User;
import com.example.rekreativ.model.enums.PlayerType;
import com.example.rekreativ.repository.UserRepository;
import com.example.rekreativ.service.RoleService;
import com.example.rekreativ.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Set;
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
    private final UserMapper userMapper;
    private final ReviewMapper reviewMapper;

    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder,
                           CustomValidator customValidator,
                           UserMapper userMapper,
                           ReviewMapper reviewMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.customValidator = customValidator;
        this.userMapper = userMapper;
        this.reviewMapper = reviewMapper;
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

        User user = findRawUserByUsername(username);
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

    @Override
    @Transactional
    public UserResponseDTO addReviewToUser(Long userId, ReviewRequestDto reviewRequest) {
        log.debug("calling addReviewToUser method");

        User user = findRawUserById(userId);

        Review review = reviewMapper.mapToReview(reviewRequest);
        review.setReviewDate(Instant.now());
        review.setPlayer(user.getPlayer());
        user.getPlayer().getReviews().add(review);

        return initSave(user);
    }

    public UserResponseDTO saveUser(UserLoginDTO userRequest) {
        log.debug("calling saveUser method");

        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            log.debug("user already exists with username: {}", userRequest.getUsername());
            throw new ObjectAlreadyExistsException(User.class, userRequest.getUsername());
        }

        Role role = roleService.findByName("ROLE_USER");
        Role roleAdmin = roleService.findByName("ROLE_ADMIN");

        User user = new User();
        user.getRoles().add(role);
        user.getRoles().add(roleAdmin);
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        customValidator.validate(user);

        Player player = new Player();
        player.setUser(user);
        user.setPlayer(player);

        return userMapper.mapToUserResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDTO initSave(User user) {
        log.debug("calling initSave method");

        return userMapper.mapToUserResponseDto(userRepository.save(user));
    }

    public void deleteUserById(Long id) {
        log.debug("calling deleteUserById method");

        userRepository.findById(id)
                .ifPresentOrElse(user -> {
                    log.info("user found with id: {}", id);
                    userRepository.deleteById(user.getId());
                    log.info("user deleted successfully!");
                }, () -> log.info("Unable to complete deletion! User not found with id: {}", id));
    }

    public Iterable<UserResponseDTO> findAll() {
        log.debug("calling findAll method");

        Iterable<User> users = userRepository.findAll();

        return StreamSupport.stream(users.spliterator(), false)
                .map(userMapper::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    public Page<UserResponseDTO> findAllPageable(Pageable pageable) {
        log.debug("calling findAllPageable method");

        return userRepository.findAll(pageable).map(userMapper::mapToUserResponseDto);
    }

    public UserResponseDTO findUserById(Long id) {
        log.debug("calling findUserById method");

        return userMapper.mapToUserResponseDto(findRawUserById(id));
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

    public UserResponseDTO findUserByUsername(String username) {
        log.debug("calling findUserByUsername method");

        return userMapper.mapToUserResponseDto(findRawUserByUsername(username));
    }

    public User findRawUserByUsername(String username) {
        log.debug("calling findRawUserByUsername method");

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(User.class, username));
    }

    public User findRawUserById(Long id) {
        log.debug("calling findRawUserById method");

        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(User.class, id));
    }

    @Override
    public UserResponseDTO addPlayerTypeToUser(UserPlayerTypeRequestDto userPlayerTypeRequestDto) {
        User user = findRawUserById(userPlayerTypeRequestDto.getUserId());
        return initSave(user);
    }
}
