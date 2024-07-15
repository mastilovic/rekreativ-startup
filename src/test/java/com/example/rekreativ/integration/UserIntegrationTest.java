package com.example.rekreativ.integration;

import com.example.rekreativ.model.dto.UserLoginDTO;
import com.example.rekreativ.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Disabled
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper mapper;

    UserLoginDTO userLoginDTO;
    User user;

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:5.7");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeEach
    void initData() {
        mapper = new ObjectMapper();
        user = new User();
        userLoginDTO = new UserLoginDTO();

        userLoginDTO.setUsername("admin");
        userLoginDTO.setPassword("admin");

        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("testuser");
    }

    @Test
    void should_RegisterSuccessfully() throws Exception {

        ResultActions usersResult = mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)));

        usersResult.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void should_LoginSuccessfully() throws Exception {

        ResultActions usersResult = mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLoginDTO)));

        usersResult.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
