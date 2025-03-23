package com.example.rekreativ.service;

import com.example.rekreativ.dto.ReviewRequestDto;
import com.example.rekreativ.dto.UserDTO;
import com.example.rekreativ.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void addRoleToUser(String username, String roles);

    UserDTO saveUser(User user);

    UserDTO initSave(User user);

    void deleteUserById(Long id);

    Iterable<UserDTO> findAll();

    Page<UserDTO> findAllPageable(Pageable pageable);

    UserDTO findUserById(Long id);

    boolean existsById(Long id);

    boolean existsByUsername(String username);

    UserDTO findUserByUsername(String username);

    UserDTO addReviewToUser(Long userId, ReviewRequestDto reviewRequest);

    User findRawUserByUsername(String username);
}
