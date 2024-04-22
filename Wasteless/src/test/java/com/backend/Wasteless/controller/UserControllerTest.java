package com.backend.Wasteless.controller;

import com.backend.Wasteless.model.User;
import com.backend.Wasteless.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUserSuccess() throws Exception {
        User user = new User("test_pass", "Success", "test@gmail.com", "password");
        user.setPoints(0);
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"test_pass\", \"name\":\"Success\", \"email\":\"test@gmail.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk());

        String result = userController.createUser(user);
        assertEquals("Successfully saved " + user.getUserName(), result);
    }

    @Test
    void userLoginWithIncorrectUsername() throws Exception {
        User user = new User("test_fail", "Success", "test@gmail.com", "password");
        HashMap<String, Object> result = new HashMap<>();
        result.put("user", "randomString");
        result.put("error", new Error("User account does not exist."));
        when(userService.verifyCredentials(any(User.class))).thenThrow(new RuntimeException("User account does not exist."));

        mockMvc.perform(post("/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"test_pass\", \"name\":\"Success\", \"email\":\"test@gmail.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/userLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"test_fail\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").isEmpty())
                .andExpect(jsonPath("$.error.message").value("User account does not exist."));
    }


    @Test
    void userLoginWithIncorrectPassword() throws Exception {
        User user = new User("test_pass", "Success", "test@gmail.com", "hashedPassword");
        User incorrectPassword = new User("test_pass", "Success", "test@gmail.com", "wrongPassword");
        HashMap<String, Object> result = new HashMap<>();
        result.put("user", null);
        result.put("error", new Error("Incorrect Password."));
        when(userService.verifyCredentials(any(User.class))).thenThrow(new RuntimeException("Incorrect Password."));

        mockMvc.perform(post("/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"test_pass\", \"name\":\"Success\", \"email\":\"test@gmail.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/userLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"test_pass\", \"password\":\"wrongPassword\"}"))
                .andExpect(status().isOk());

        HashMap<String, Object> expectedResult = userController.verifyCredentials(incorrectPassword);
        assertEquals(result.get("user"), expectedResult.get("user"));
        Object expectedError = result.get("error");
        Object actualError = expectedResult.get("error");
        assertEquals(expectedError.toString(), actualError.toString());
    }

    @Test
    void getAllUser() throws Exception {
        User user = new User("test_pass", "Success", "test@gmail.com", "password");
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/getAllUser"))
                .andExpect(status().isOk());

        assertEquals(List.of(user), userController.getAllUser());
    }

    @Test
    void getUser() throws Exception {
        User user = new User("test_pass", "Success", "test@gmail.com", "password");
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/getUser").param("userName", "test_pass"))
                .andExpect(status().isOk());

        assertEquals(Optional.of(user), userController.getUser("test_pass"));
    }
}
