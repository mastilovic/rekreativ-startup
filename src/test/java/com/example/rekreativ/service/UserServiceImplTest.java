package com.example.rekreativ.service;

import com.example.rekreativ.model.User;
import com.example.rekreativ.repository.UserRepository;
import com.example.rekreativ.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
//    @Autowired
    @InjectMocks private UserServiceImpl underTest;// = new UserService(userRepository);

//    @BeforeEach
//    void setUp() {
//        underTest = new UserServiceImpl(userRepository, roleService, passwordEncoder);
//    }

    @Test
    @Disabled
    void loadUserByUsername() {
    }

    @Test
    @Disabled
    void addRoleToUser() {
    }

    // TODO
//    @Test
//    void canSaveUser() {
//        // given
//        User testUser = new User(null, "testUser", "testPassword");
//        // when
//        underTest.saveUser(testUser);   // using userRepository instead of UserService
//                                        // because underTest returns null
//                                        // when using setters for password
//                                        // and role
//
//        //then
//        ArgumentCaptor<User> userArgumentCaptor =
//                ArgumentCaptor.forClass(User.class);
//
//        verify(userRepository).save(userArgumentCaptor.capture());
//
//        User capturedUser = userArgumentCaptor.getValue();
//
//        assertThat(capturedUser).isEqualTo(testUser);
//    }

    @Test
    void deleteUserById() {
        //given
        User testUser = new User(null,"test","test");
        userRepository.save(testUser);
        //when
        underTest.deleteUserById(testUser.getId());
        //then
        verify(userRepository).deleteById(testUser.getId());
    }

    @Test
    @Disabled
    void delete() {
    }

    @Test
    void findAll() {
        // when
        underTest.findAll();
        // then
        verify(userRepository).findAll();
    }

    @Test
    void findUserById() {
        // give
        User testUser = new User(null,"test","test");
        // when
        underTest.findUserById(testUser.getId());
        // then
        verify(userRepository).findById(testUser.getId());
    }

    @Test
    void findUserByUsername() {
    }
}