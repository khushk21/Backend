package com.backend.Wasteless.controller;

import com.backend.Wasteless.model.User;
import com.backend.Wasteless.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepo;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addUser() throws Exception{
        User user = new User("Test", "01");
        when(userRepo.save(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/addUser").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\", \"rollNumber\":\"01\"}")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.rollNumber").value("01"));

    }

    @Test
    void getAllUser() throws Exception{
        List<User> userList = Arrays.asList(new User("Khush", "01"), new User("Test", "02"));
        System.out.println("Hello" +userList);
        when(userRepo.findAll()).thenReturn(userList);
        mockMvc.perform(get("/getAllUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Khush"))
                .andExpect(jsonPath("$[0].rollNumber").value("01"))
                .andExpect(jsonPath("$[1].name").value("Test"))
                .andExpect(jsonPath("$[1].rollNumber").value("02"));
    }

}