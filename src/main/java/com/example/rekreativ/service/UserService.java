package com.example.rekreativ.service;

import com.example.rekreativ.dto.UserDTO;
import com.example.rekreativ.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void addRoleToUser(String username, String roles);

    User saveUser(User user);

    User initSave(User user);

    void deleteUserById(Long id);

    void delete(User User);

    Iterable<UserDTO> findAll();

    Page<User> findAllPageable(Pageable pageable);

    User findUserById(Long id);

    boolean existsById(Long id);

    boolean existsByUsername(String username);

    User findUserByUsername(String username);
}
