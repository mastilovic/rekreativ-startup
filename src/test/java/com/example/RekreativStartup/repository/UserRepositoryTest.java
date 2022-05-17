package com.example.RekreativStartup.repository;

import com.example.RekreativStartup.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;


    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void itShouldFindUserByUsername() {
        // give
        User testUser = new User(null,
                "testUsername",
                "testPassword");
        underTest.save(testUser);
        // when
        Optional<User> testFindUserByUsername = underTest.findByUsername(testUser.getUsername());
        // then
        assertThat(testFindUserByUsername).isPresent();

    }

    @Test
    void checkIfUserCanBeCreatedNullObject(){
        // givem

        // passing null as id because GeneratedValue
        // would auto generate new id
        User nullUser = new User(null,
                null,
                null);

        // when
        Optional<User> existingNullUser = underTest.findByUsername(nullUser.getUsername());

        // then
        assertThat(existingNullUser).isEmpty();
    }

    @Test
    void checkIfUserCanBeCreatedAsEmptyObject(){

        // given

        // passing null as id because GeneratedValue
        // will auto generate new id
        User emptyUser = new User (null,
                " ",
                " ");

        // when
        Optional<User> existingEmptyUser = underTest.findByUsername(emptyUser.getUsername());

        // then
        assertThat(existingEmptyUser).isEmpty();
    }
}