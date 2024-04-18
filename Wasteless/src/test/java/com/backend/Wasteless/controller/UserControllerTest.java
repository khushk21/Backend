package com.backend.Wasteless.controller;

import com.backend.Wasteless.model.User;
import com.backend.Wasteless.repository.UserRepository;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        User user = new User.Builder("test_pass", "password", "Success","test@gmail.com").build();
//        User user = new User("test_pass", "Success", "test@gmail.com", "password");
        user.setPoints(0);
        when(userRepo.findById(user.getUserName())).thenReturn(Optional.empty());
        when(userRepo.save(any(User.class))).thenReturn(user);
//        mockMvc.perform(post("/registerUser").contentType(MediaType.APPLICATION_JSON)
//                .content("{\"userName\":\"test_pass\", \"name\":\"Success\", \"email\":\"test@gmail.com\", \"password\":\"password\"}"))
//                .andExpect(status().isOk());
        String result = userController.createUser(user);
        assertEquals("Successfully saved " + user.getUserName(), result);
    }

//    @Test
//    void createUserWithExistingUsername() {
//        //TODO: Add password hashing test and logic before adding password
//        User user = new User("test_pass", "Success", "test@gmail.com", "password");
//        when(userRepo.findById(user.getUserName())).thenReturn(Optional.of(user));
//        String result = userController.createUser(user);
//        assertEquals("Username already exists. Please try again.", result);
//    }
//
//    @Test
//    void userLoginSuccess() throws Exception {
//        //TODO: Add password hashing test and logic before adding password
//        String hashedPasswordUser = UserController.hashPassword("password");
//
//        User user = new User("test_pass", "Success", "test@gmail.com", hashedPasswordUser);
//        HashMap <String, Object> result = new HashMap<String, Object>();
//        when(userRepo.save(any(User.class))).thenReturn(user);
//        result.put("user", user);
//        result.put("error", null);
//
//        mockMvc.perform(post("/userLogin").contentType(MediaType.APPLICATION_JSON)
//                .content("{\"userName\":\"test_pass\", \"password\":\"" + hashedPasswordUser + "\"}"));
//        when(userRepo.findById(user.getUserName())).thenReturn(Optional.of(user));
//
//        HashMap <String, Object> expectedResult = userController.verifyCredentials(user);
//        System.out.println("User: " + user);
//        System.out.println("result : " + result);
//        System.out.println("expectedResult" + expectedResult);
//        assertEquals(result.get("user"), expectedResult.get("user"));
//        assertEquals(result.get("error"), expectedResult.get("error"));
//    }

    @Test
    void userLoginWithIncorrectUsername() throws Exception {
        //TODO: Add password hashing test and logic before adding password
        User user = new User.Builder("test_fail", "password", "Success","test@gmail.com").build();
        //User user = new User("test_fail", "Success", "test@gmail.com", "password");
        HashMap <String, Object> result = new HashMap<String, Object>();
        result.put("user", null);
        result.put("error", new Error("User account does not exist."));
        when(userRepo.save(any(User.class))).thenReturn(user);
//        mockMvc.perform(post("/registerUser").contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"userName\":\"test_pass\", \"name\":\"Success\", \"email\":\"test@gmail.com\", \"password\":\"password\"}"))
//                .andExpect(status().isOk());
//        mockMvc.perform(post("/userLogin").contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"userName\":\"test_pass\", \"password\":\"password\"}")).andExpect(status().isOk());
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
        //User user = new User("test_pass", "Success", "test@gmail.com", "40cbb2a50a0ee1c4ddbb34bdbf284c47afcd47e4767c6ecdf12ba7cbeb9890c3adeae599a0cba2d69398b68d561faa7846b3a9cf137936ab603e3e285e54759a:e6ff96d1ab336427b01290d64ec697f4");
        User user = new User.Builder("test_pass", "40cbb2a50a0ee1c4ddbb34bdbf284c47afcd47e4767c6ecdf12ba7cbeb9890c3adeae599a0cba2d69398b68d561faa7846b3a9cf137936ab603e3e285e54759a:e6ff96d1ab336427b01290d64ec697f4","Success","test@gmail.com").build();
        User incorrectPassword = new User.Builder("test_pass", "password_wrong", "Success","test@gmail.com").build();
        //User incorrectPassword = new User("test_pass", "Success", "test@gmail.com", "password_wrong");
        HashMap <String, Object> result = new HashMap<String, Object>();
        result.put("user", null);
        result.put("error", new Error("Incorrect Password."));
        when(userRepo.save(any(User.class))).thenReturn(user);
//        mockMvc.perform(post("/registerUser").contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"userName\":\"test_pass\", \"name\":\"Success\", \"email\":\"test@gmail.com\", \"password\":\"password\"}"))
//                .andExpect(status().isOk());
//        mockMvc.perform(post("/userLogin").contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"userName\":\"test_pass\", \"password\":\"password_wrong\"}"))
//                .andExpect(status().isOk());
        when(userRepo.findById(user.getUserName())).thenReturn(Optional.of(user));
        HashMap <String, Object> expectedResult = userController.verifyCredentials(incorrectPassword);
        assertEquals(result.get("user"), expectedResult.get("user"));
        Object expectedError = result.get("error");
        Object actualError = expectedResult.get("error");
        assertEquals(expectedError.toString(), actualError.toString());
    }

}