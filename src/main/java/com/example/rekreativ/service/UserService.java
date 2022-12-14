package com.example.rekreativ.service;

import com.example.rekreativ.dto.UserDTO;
import com.example.rekreativ.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    public void initRoleAndUser();

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public void addRoleToUser(String username, String roles);

    public User saveUser(User user);

    public User initSave(User user);

    public void deleteUserById(Long id);

    public void delete(User User);

    public Iterable<UserDTO> findAll();

    public Page<User> findAllPageable(Pageable pageable);

    public User findUserById(Long id);

    public boolean existsById(Long id);

    public boolean existsByUsername(String username);

    public User findUserByUsername(String username);

}
