package com.backend.Wasteless.controller;

import com.backend.Wasteless.constants.WasteCategory;
import com.backend.Wasteless.model.WasteRecord;
import com.backend.Wasteless.service.UserService;
import com.backend.Wasteless.service.WasteRecordService;
import com.backend.Wasteless.repository.UserRepository;
import com.backend.Wasteless.repository.WasteRecordRepository;
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
import com.backend.Wasteless.model.User;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.DataFormatException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class WasteRecordControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserRepository userRepo;
    @Mock
    private WasteRecordRepository wasteRecordRepo;
    @Mock
    private UserService userService;
    @Mock
    private WasteRecordService wasteRecordService;
    @InjectMocks
    private WasteRecordController wasteRecordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(wasteRecordController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addNewRecordSuccessWithFirstWasteRecord() throws Exception {
        User user = new User("test_pass", "Success", "test@gmail.com", "password");
        user.setPoints(0);
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", user.getUserName());
        payload.put("dateTime", "2024-02-25T21:47:30");
        payload.put("category","E_WASTE");
        payload.put("weight", "2.5");

        WasteRecord wasteRecord = new WasteRecord(LocalDateTime.parse((String) payload.get("dateTime")), WasteCategory.E_WASTE, Double.parseDouble((String) payload.get("weight")));
        user.setWasteRecords(List.of(wasteRecord));
        when(wasteRecordService.addNewRecord(any())).thenReturn(user);

        HashMap<String, Object> actualResult = wasteRecordController.addNewRecord(payload);
        HashMap<String, Object> expectedResult = new HashMap<String, Object>();
        expectedResult.put("user", user);
        expectedResult.put("error", null);

        assertEquals(expectedResult.get("user"), actualResult.get("user"));

        User actualUser = (User) actualResult.get("user");
        assertEquals(actualUser.getWasteRecords().toArray().length, 1);
        assertEquals(actualUser.getPoints(), user.getPoints());
        assertEquals(expectedResult.get("error"), actualResult.get("error"));
    }


    @Test
    void addNewRecordSuccessWithMultipleWasteRecords() throws Exception{
        User user = new User("test_pass", "Success", "test@gmail.com", "password");
        user.setPoints(70);
        Map<String, Object> payload = new HashMap<>();
        payload.put("userame", user.getUserName());
        payload.put("dateTime", "2024-02-25T21:47:30");
        payload.put("category","E_WASTE");
        payload.put("weight", "2.5");
        WasteRecord wasteRecord_1 = new WasteRecord(LocalDateTime.parse((String) payload.get("dateTime")), WasteCategory.E_WASTE, Double.parseDouble((String) payload.get("weight")));
        WasteRecord wasteRecord_2 = new WasteRecord(LocalDateTime.parse("2024-02-26T21:47:30"), WasteCategory.NORMAL_WASTE, 10);
        user.setWasteRecords(List.of(wasteRecord_1, wasteRecord_2));
        when(wasteRecordService.addNewRecord(any())).thenReturn(user);
        HashMap<String, Object> actualResult = wasteRecordController.addNewRecord(payload);
        HashMap<String, Object> expectedResult = new HashMap<String, Object>();
        expectedResult.put("user", user);
        expectedResult.put("error", null);
        assertEquals(expectedResult.get("user"), actualResult.get("user"));
        User actualUser = (User) actualResult.get("user");
        assertEquals(user.getWasteRecords().toArray().length, actualUser.getWasteRecords().toArray().length);
        assertEquals(actualUser.getPoints(), user.getPoints());
        assertEquals(expectedResult.get("error"), actualResult.get("error"));
    }

    @Test
    void addNewRecordFailureUsernameNotFound() throws Exception{
        User user = new User("test_pass", "Failure", "test@gmail.com", "password");
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", user.getUserName());
        payload.put("dateTime", "2024-02-25T21:47:30");
        payload.put("category","E_WASTE");
        payload.put("weight", "2.0");
        when(wasteRecordService.addNewRecord(any())).thenThrow(new IllegalArgumentException("Incorrect Username provided."));
        HashMap <String, Object> expectedResult = new HashMap<String, Object>();
        expectedResult.put("user", null);
        expectedResult.put("error", new Error("Incorrect Username provided."));
        HashMap<String, Object> actualResult = wasteRecordController.addNewRecord(payload);
        Object expectedError = expectedResult.get("error");
        Object actualError = actualResult.get("error");
        assertEquals(expectedError.toString(), actualError.toString());
        assertEquals(expectedResult.get("user"), actualResult.get("user"));
    }

    @Test
    void addNewRecordFailureUserNotSaved() throws Exception{
        User user = new User("test_pass", "Failure", "test@gmail.com", "password");
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", user.getUserName());
        payload.put("dateTime", "2024-02-25T21:47:30");
        payload.put("category","E_WASTE");
        payload.put("weight", "2.5");
        when(wasteRecordService.addNewRecord(any())).thenThrow(new IllegalArgumentException("Error in saving user records."));
        HashMap <String, Object> expectedResult = new HashMap<String, Object>();
        expectedResult.put("user", null);
        expectedResult.put("error", new Error("Error in saving user records."));
        HashMap<String, Object> actualResult = wasteRecordController.addNewRecord(payload);
        Object expectedError = expectedResult.get("error");
        Object actualError = actualResult.get("error");
        assertEquals(expectedError.toString(), actualError.toString());
        assertEquals(expectedResult.get("user"), actualResult.get("user"));
    }

    @Test
    void addNewRecordFailureInvalidDateTime() throws Exception{
        User user = new User("test_pass", "Failure", "test@gmail.com", "password");
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", user.getUserName());
        payload.put("dateTime", "2024-23");
        payload.put("category","E_WASTE");
        payload.put("weight", "2.5");
        when(wasteRecordService.addNewRecord(any())).thenThrow(new IllegalArgumentException("Invalid timestamp provided."));
        HashMap <String, Object> expectedResult = new HashMap<String, Object>();
        expectedResult.put("user", null);
        expectedResult.put("error", new Error("Invalid timestamp provided."));
        HashMap<String, Object> actualResult = wasteRecordController.addNewRecord(payload);
        Object expectedError = expectedResult.get("error");
        Object actualError = actualResult.get("error");
        assertEquals(expectedError.toString(), actualError.toString());
        assertEquals(expectedResult.get("user"), actualResult.get("user"));
    }

    @Test
    void addNewRecordFailureInvalidWasteCategory() throws Exception{
        User user = new User("test_pass", "Failure", "test@gmail.com", "password");
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", user.getUserName());
        payload.put("dateTime", "2024-02-25T21:47:30");
        payload.put("category","Incorrect Category");
        payload.put("weight", "2.5");
        when(wasteRecordService.addNewRecord(any())).thenThrow(new IllegalArgumentException("Invalid waste category provided."));
        HashMap <String, Object> expectedResult = new HashMap<String, Object>();
        expectedResult.put("user", null);
        expectedResult.put("error", new Error("Invalid waste category provided."));
        HashMap<String, Object> actualResult = wasteRecordController.addNewRecord(payload);
        Object expectedError = expectedResult.get("error");
        Object actualError = actualResult.get("error");
        assertEquals(expectedError.toString(), actualError.toString());
        assertEquals(expectedResult.get("user"), actualResult.get("user"));
    }

    @Test
    void calculatePointsEWaste() throws Exception{
        when(wasteRecordService.calculatePoints(2, WasteCategory.E_WASTE)).thenReturn(6);
        int points = wasteRecordService.calculatePoints(2, WasteCategory.E_WASTE);
        assertEquals(points, 6);
    }

    @Test
    void calculatePointsNormalWaste() throws Exception{
        when(wasteRecordService.calculatePoints(2, WasteCategory.NORMAL_WASTE)).thenReturn(14);
        int points = wasteRecordService.calculatePoints(2, WasteCategory.NORMAL_WASTE);
        assertEquals(points, 14);
    }

    @Test
    void calculatePointsLightingWaste() throws Exception{
        when(wasteRecordService.calculatePoints(2, WasteCategory.LIGHTING_WASTE)).thenReturn(8);
        int points = wasteRecordService.calculatePoints(2, WasteCategory.LIGHTING_WASTE);
        assertEquals(points, 8);
    }

    @Test
    void calculatePointsWasteTreatment() throws Exception{
        when(wasteRecordService.calculatePoints(2, WasteCategory.WASTE_TREATMENT)).thenReturn(12);
        int points = wasteRecordService.calculatePoints(2, WasteCategory.WASTE_TREATMENT);
        assertEquals(points, 12);
    }

    @Test
    void calculatePointsCashForTrash() throws Exception{
        when(wasteRecordService.calculatePoints(2, WasteCategory.CASH_FOR_TRASH)).thenReturn(18);
        int points = wasteRecordService.calculatePoints(2, WasteCategory.CASH_FOR_TRASH);
        assertEquals(points, 18);
    }
}