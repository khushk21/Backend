package com.backend.Wasteless.controller;

import com.backend.Wasteless.model.User;
import com.backend.Wasteless.repository.UserRepository;

import java.util.*;

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
    void createUserSuccess() throws Exception{
        //TODO: Add password hashing test and logic before adding password
        User user = new User("test_pass", "Success", "test@gmail.com", "password");
        user.setPoints(0);
        when(userRepo.findById(user.getUserName())).thenReturn(Optional.empty());
        when(userRepo.save(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/registerUser").contentType(MediaType.APPLICATION_JSON)
                .content("{\"userName\":\"test_pass\", \"name\":\"Success\", \"email\":\"test@gmail.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk());
        String result = userController.createUser(user);
        assertEquals("Successfully saved " + user.getUserName(), result);
    }

    @Test
    void createUserWithExistingUsername() {
        //TODO: Add password hashing test and logic before adding password
        User user = new User("test_pass", "Success", "test@gmail.com", "password");
        when(userRepo.findById(user.getUserName())).thenReturn(Optional.of(user));
        String result = userController.createUser(user);
        assertEquals("Username already exists. Please try again.", result);
    }

    @Test
    void userLoginSuccess() throws Exception {
        //TODO: Add password hashing test and logic before adding password
        User user = new User("test_pass", "Success", "test@gmail.com", "password");
        HashMap <String, Object> result = new HashMap<String, Object>();
        when(userRepo.save(any(User.class))).thenReturn(user);
        result.put("user", user);
        result.put("error", null);
        mockMvc.perform(post("/registerUser").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"test_pass\", \"name\":\"Success\", \"email\":\"test@gmail.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/userLogin").contentType(MediaType.APPLICATION_JSON)
                .content("{\"userName\":\"test_pass\", \"password\":\"password\"}")).andExpect(status().isOk());
        when(userRepo.findById(user.getUserName())).thenReturn(Optional.of(user));
        HashMap <String, Object> expectedResult = userController.verifyCredentials(user);
        assertEquals(result.get("user"), expectedResult.get("user"));
        assertEquals(result.get("error"), expectedResult.get("error"));
    }

    @Test
    void userLoginWithIncorrectUsername() throws Exception {
        //TODO: Add password hashing test and logic before adding password
        User user = new User("test_fail", "Success", "test@gmail.com", "password");
        HashMap <String, Object> result = new HashMap<String, Object>();
        result.put("user", null);
        result.put("error", new Error("User account does not exist."));
        when(userRepo.save(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/registerUser").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"test_pass\", \"name\":\"Success\", \"email\":\"test@gmail.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/userLogin").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"test_pass\", \"password\":\"password\"}")).andExpect(status().isOk());
        when(userRepo.findById(user.getUserName())).thenReturn(Optional.empty());
        HashMap <String, Object> expectedResult = userController.verifyCredentials(user);
        assertEquals(result.get("user"), expectedResult.get("user"));
        Object expectedError = result.get("error");
        Object actualError = expectedResult.get("error");
        assertEquals(expectedError.toString(), actualError.toString());
    }

    @Test
    void userLoginWithIncorrectPassword() throws Exception{
        //TODO: Add password hashing test and logic before adding password
        User user = new User("test_pass", "Success", "test@gmail.com", "password");
        User incorrectPassword = new User("test_pass", "Success", "test@gmail.com", "password_wrong");
        HashMap <String, Object> result = new HashMap<String, Object>();
        result.put("user", null);
        result.put("error", new Error("Incorrect Password."));
        when(userRepo.save(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/registerUser").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"test_pass\", \"name\":\"Success\", \"email\":\"test@gmail.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/userLogin").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"test_pass\", \"password\":\"password_wrong\"}"))
                .andExpect(status().isOk());
        when(userRepo.findById(user.getUserName())).thenReturn(Optional.of(user));
        HashMap <String, Object> expectedResult = userController.verifyCredentials(incorrectPassword);
        assertEquals(result.get("user"), expectedResult.get("user"));
        Object expectedError = result.get("error");
        Object actualError = expectedResult.get("error");
        assertEquals(expectedError.toString(), actualError.toString());
    }

}