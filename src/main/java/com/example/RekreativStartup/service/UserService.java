package com.example.RekreativStartup.service;

import com.example.RekreativStartup.auth.AuthUserDetails;
import com.example.RekreativStartup.model.Role;
import com.example.RekreativStartup.model.User;
import com.example.RekreativStartup.repository.RoleRepository;
import com.example.RekreativStartup.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

//    @Autowired
    private final UserRepository userRepository; //injecting through constructor for testing
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void initRoleAndUser() {

        if(roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }


        if(roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }

        if(userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
            final String adminpass = "admin";
            User adminUser = new User(
                    null,
                    "admin",
                    passwordEncoder.encode(adminpass));
            adminUser.getRoles().add(adminRole);
            userRepository.save(adminUser);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Username '%s' was not found",username)));
        AuthUserDetails authUserDetails = new AuthUserDetails(user);

        return authUserDetails;
    }

    //need to validate email, roles etc
    public void addRoleToUser(String username, String roles){
        User user = userRepository.findByUsername(username).get();
        Role role = roleRepository.findByName(roles).get();
        user.getRoles().add(role);
//		log.info("Adding role {} to the user {}", roles, email);
    }

    //--------------------Save User with default User role--------------------
    public User saveUser(User user) {
		log.info("Saving new user {} to the database", user.getUsername());
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(null);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRoles(userRoles);
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }


    //--------------------------------------------------------------------
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void delete(User User) {
        userRepository.delete(User);
    }

//    public User findUserById(Long id) {
//        return userRepository.findById(id).orElse(null);
//    }

    public Iterable<User> findAll(){
        return userRepository.findAll();
    }

    public Page<User> findAllPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

}
