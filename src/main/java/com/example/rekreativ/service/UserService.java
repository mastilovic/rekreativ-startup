package com.example.rekreativ.service;

import com.example.rekreativ.model.dto.UserLoginDTO;
import com.example.rekreativ.model.dto.request.ReviewRequestDto;
import com.example.rekreativ.model.dto.request.UserPlayerTypeRequestDto;
import com.example.rekreativ.model.dto.response.UserResponseDTO;
import com.example.rekreativ.model.User;
import com.example.rekreativ.model.enums.PlayerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserService {

    void addRoleToUser(String username, String roles);

    UserResponseDTO saveUser(UserLoginDTO user);

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

    UserResponseDTO addPlayerTypeToUser(UserPlayerTypeRequestDto userPlayerTypeRequestDto);
}
