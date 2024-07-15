package com.example.rekreativ.controller;


import com.example.rekreativ.commons.AuthService;
import com.example.rekreativ.model.dto.request.ReviewRequestDto;
import com.example.rekreativ.model.dto.request.UserPlayerTypeRequestDto;
import com.example.rekreativ.model.dto.response.UserResponseDTO;
import com.example.rekreativ.model.dto.UserLoginDTO;
import com.example.rekreativ.model.User;
import com.example.rekreativ.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService,
                          AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO user) {
        authService.authenticate(user.getUsername(), user.getPassword());
        UserResponseDTO loginUser = userService.findUserByUsername(user.getUsername());
        HttpHeaders jwtHeader = authService.getJwtHeader(user.getUsername());
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(path = "/role", method = RequestMethod.POST)
    public ResponseEntity<?> addRoleToUser(@RequestParam(value = "username", required = false) String username,
                                           @RequestParam(value = "roles", required = false) String roles) {
        userService.addRoleToUser(username, roles);
        return new ResponseEntity<>("Role successfully added to user!", OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }


    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(@RequestBody User user) {
//        User existingUser = userService.findUserById(user.getId());
//
//        existingUser.setUsername(user.getUsername());
//        existingUser.setPassword(user.getPassword());
//        userService.saveUser(existingUser); //todo: create update method for user in service

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/review/{userId}", method = RequestMethod.POST)
    public ResponseEntity<?> addReviewToUser(@PathVariable("userId") Long userId, @RequestBody ReviewRequestDto review) {
        return ResponseEntity.ok(userService.addReviewToUser(userId, review));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/type", method = RequestMethod.POST)
    public ResponseEntity<?> addPlayerTypeToUser(@RequestBody UserPlayerTypeRequestDto userPlayerTypeRequestDto) {
        return ResponseEntity.ok(userService.addPlayerTypeToUser(userPlayerTypeRequestDto));
    }
}
