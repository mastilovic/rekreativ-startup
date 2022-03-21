package com.example.RekreativStartup.controller;


import com.example.RekreativStartup.Service.UserService;
import com.example.RekreativStartup.auth.AuthUserDetails;
import com.example.RekreativStartup.model.User;
import com.example.RekreativStartup.repository.RoleRepository;
import com.example.RekreativStartup.util.JwtUtil;
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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/api/user/v1")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;
//    private ValidatorUtil validatorUtil;

    @Autowired
    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    public void initRolesAndUser() {
        if (!roleRepository.findByName("ROLE_USER").isPresent()
                && !roleRepository.findByName("ROLE_ADMIN").isPresent()) {
            userService.initRoleAndUser();
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername()).get();
        AuthUserDetails authUserDetails = new AuthUserDetails(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(authUserDetails);

//        ModelMapper modelMapper = new ModelMapper();
//        UserDTO userDto = modelMapper.map(loginUser, UserDTO.class);

        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    private HttpHeaders getJwtHeader(AuthUserDetails user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Jwt-Token", jwtUtil.generateJwtToken(user));

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

        Optional<User> userObj = userService.findUserByUsername(username);
        List<String> userRoles = new ArrayList<>();

        userObj.map(User::getRoles).get().forEach(role -> {
            userRoles.add(role.getName());
        });
        if (userRoles.contains(roles)) {
            // log.error("User already has that role!");

            return new ResponseEntity<Object>("User already has that role!", HttpStatus.BAD_REQUEST);
        } else {
            userService.addRoleToUser(username, roles);
        }

        return new ResponseEntity<Object>("Role successfully added to user!", OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {

        User obj = userService.findUserById(id).orElse(null);
        if (obj == null) {

            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
//
//        ModelMapper modelMapper = new ModelMapper();
//        UserDTO userDto = modelMapper.map(obj, UserDTO.class);

        return new ResponseEntity<Object>(obj, HttpStatus.OK);

    }


    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setPersonalScore(null);
        newUser.setTeam(null);
        newUser = userService.saveUser(newUser);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/update/score", method = RequestMethod.POST)
    public ResponseEntity<?> updateUserPersonalScore(@RequestBody User user) {

        User existingUser = userService.findUserByUsername(user.getUsername()).orElse(null);
        if (existingUser == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }

        existingUser.setPersonalScore(user.getPersonalScore());
        userService.saveUser(existingUser);
        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(@RequestBody User user) {

        User existingUser = userService.findUserById(user.getId()).orElse(null);
        if (existingUser == null) {
            return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setPersonalScore(user.getPersonalScore());
        existingUser.setTeam(user.getTeam());
        userService.saveUser(existingUser);

        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {

        Iterable<User> obj = userService.findAll();
//        ArrayList myList = new ArrayList();
//        for (User u: obj){
//            myList.add(u);
//        }
//        if (obj == null) {
//
//            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
//        }

//        Object[] objArray = myList.toArray();
//        ArrayList dtoList = new ArrayList();
//        ModelMapper modelMapper = new ModelMapper();
//        for(int i=0; i < objArray.length ; i++) {
//            UserDTO userDto = modelMapper.map(objArray[i], UserDTO.class);
//            dtoList.add(userDto);
//        }

        return new ResponseEntity<Object>(obj, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<Object>("User deleted successfully!", HttpStatus.OK);
    }
}
