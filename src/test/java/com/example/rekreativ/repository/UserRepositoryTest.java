package com.example.rekreativ.repository;

import com.example.rekreativ.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:5.7")
            .withDatabaseName("testdb")
            .withUsername("root")
            .withPassword("root");

    @DynamicPropertySource
    public static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.uri", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    User userOne, emptyUser;

    @BeforeEach
    void setUp() {
        userOne = new User(null,
                "testUsername",
                "testPassword");

        emptyUser = new User(null,
                " ",
                " ");

        underTest.save(userOne);
    }

    @AfterEach
    void clearDb(){
        underTest.deleteAll();
    }

    @Test
    void itShouldFindUserByUsername() {
        Optional<User> user = underTest.findByUsername(userOne.getUsername());

        assertThat(user).isPresent();
    }
}