package com.example.rekreativ.service;

import com.example.rekreativ.dto.request.ReviewRequestDto;
import com.example.rekreativ.dto.response.UserResponseDTO;
import com.example.rekreativ.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void addRoleToUser(String username, String roles);

    UserResponseDTO saveUser(User user);

    UserResponseDTO initSave(User user);

    void deleteUserById(Long id);

    Iterable<UserResponseDTO> findAll();

    Page<UserResponseDTO> findAllPageable(Pageable pageable);

    UserResponseDTO findUserById(Long id);

    boolean existsById(Long id);

    boolean existsByUsername(String username);

    UserResponseDTO findUserByUsername(String username);

    UserResponseDTO addReviewToUser(Long userId, ReviewRequestDto reviewRequest);

    User findRawUserByUsername(String username);
    User findRawUserById(Long id);
}
