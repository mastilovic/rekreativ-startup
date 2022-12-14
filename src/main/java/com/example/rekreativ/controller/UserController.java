package com.example.rekreativ.controller;


import com.example.rekreativ.auth.AuthUserDetails;
import com.example.rekreativ.dto.UserDTO;
import com.example.rekreativ.model.User;
import com.example.rekreativ.repository.RoleRepository;
import com.example.rekreativ.service.UserService;
import com.example.rekreativ.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.OK;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/user/v1")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger( UserController.class.getName() );

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService,
                          RoleRepository roleRepository,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public void initRolesAndUser() {
        if (roleRepository.findByName("ROLE_USER").isEmpty()
                && roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            userService.initRoleAndUser();
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        AuthUserDetails authUserDetails = new AuthUserDetails(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(authUserDetails);

        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDto = modelMapper.map(loginUser, UserDTO.class);

        return new ResponseEntity<>(userDto, jwtHeader, OK);
    }

    private HttpHeaders getJwtHeader(AuthUserDetails user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", jwtUtil.generateJwtToken(user));

        return headers;
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(path = "/add/role/to/user", method = RequestMethod.POST)
    public ResponseEntity<?> addRoleToUser(@RequestParam(value = "username", required = false) String username,
                                           @RequestParam(value = "roles", required = false) String roles) {
        if (roleRepository.findByName(roles).isEmpty()){
            return new ResponseEntity<Object>("Role doesn't exist!", HttpStatus.BAD_REQUEST);
        }

        User user = userService.findUserByUsername(username);
        List<String> userRoles = new ArrayList<>();

        //todo: create new method that checks if user has provided role
        user.getRoles().forEach(role -> {
            userRoles.add(role.getName());
        });

        if (userRoles.contains(roles)) {
            // log.error("User already has that role!");
            return new ResponseEntity<Object>("User already has that role!", HttpStatus.BAD_REQUEST);
        }
        userService.addRoleToUser(username, roles);

        return new ResponseEntity<Object>("Role successfully added to user!", OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
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
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Iterable<UserDTO> users = userService.findAll();

        return new ResponseEntity<Object>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteUserById(id);

        return new ResponseEntity<Object>("User deleted successfully!", HttpStatus.OK);
    }
}
