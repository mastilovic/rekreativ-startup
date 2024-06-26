package com.example.rekreativ.controller;


import com.example.rekreativ.auth.AuthUser;
import com.example.rekreativ.dto.UserLoginDTO;
import com.example.rekreativ.dto.UserDTO;
import com.example.rekreativ.model.User;
import com.example.rekreativ.service.UserService;
import com.example.rekreativ.commons.JwtHandler;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtHandler jwtHandler;

    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtHandler jwtHandler) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtHandler = jwtHandler;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        AuthUser authUser = new AuthUser(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(authUser);

        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDto = modelMapper.map(loginUser, UserDTO.class);

        return new ResponseEntity<>(userDto, jwtHeader, OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(path = "/role", method = RequestMethod.POST)
    public ResponseEntity<?> addRoleToUser(@RequestParam(value = "username", required = false) String username,
                                           @RequestParam(value = "roles", required = false) String roles) {

        userService.addRoleToUser(username, roles);

        return new ResponseEntity<Object>("Role successfully added to user!", OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        User obj = userService.findUserById(id);

        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDto = modelMapper.map(obj, UserDTO.class);

        return new ResponseEntity<Object>(userDto, HttpStatus.OK);
    }


    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody User user) {

        User newUser = userService.saveUser(user);
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDto = modelMapper.map(newUser, UserDTO.class);

        return new ResponseEntity<Object>(userDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        User existingUser = userService.findUserById(user.getId());

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        userService.saveUser(existingUser); //todo: create update method for user in service

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Iterable<UserDTO> users = userService.findAll();

        return new ResponseEntity<Object>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteUserById(id);

        return new ResponseEntity<Object>("User deleted successfully!", HttpStatus.OK);
    }

    private HttpHeaders getJwtHeader(AuthUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", jwtHandler.generateJwtToken(user));

        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
