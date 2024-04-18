//package com.backend.Wasteless.controller;
//
//import com.backend.Wasteless.model.CarPark;
//import com.backend.Wasteless.service.CarParkService;
//import com.backend.Wasteless.repository.CarParkRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.io.ByteArrayInputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class CarParkControllerTest {
//
//    @InjectMocks
//    private CarParkController carParkController;
//
//    @Mock
//    private  CarParkRepository carParkRepo;
//
//    @Mock
//    private ObjectMapper objectMapper;
//    @Mock
//    private URL url;
//
//    @Mock
//    private HttpURLConnection connection;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(carParkController).build();
//    }
//
//    @Test
//    void testAddCarParkData() throws Exception {
//        // Arrange
//        when(objectMapper.readValue(anyString(), eq(Map.class))).thenReturn(Map.of("request", "createCarParkDB"));
//
//        // Act & Assert
//        mockMvc.perform(post("/addCarParkData")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"request\": \"createCarParkDB\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Car Park Database created successfully"));
//    }
//
//    @Test
//    void testGetCarPark() {
//        // Arrange
//        String sampleLine = "\"ACB\",\"BLK 270/271 ALBERT CENTRE BASEMENT CAR PARK\",,\"\",\"BASEMENT CAR PARK\",\"ELECTRONIC PARKING\",,\"YES\",\"NO\",\"NO\",\"YES\",,\"\",\"1.301062605\",\"103.8541177\"";
//
//        // Act
//        CarPark carPark = CarParkService.getCarPark(sampleLine);
//
//        // Assert
//        assertNotNull(carPark);
//        assertEquals("ACB", carPark.getCarParkNo());
//        assertEquals("BLK 270/271 ALBERT CENTRE BASEMENT CAR PARK", carPark.getAddress());
//        assertEquals("BASEMENT CAR PARK", carPark.getCarParkType());
//        assertEquals("ELECTRONIC PARKING", carPark.getParkingType());
//        assertEquals(1.301062605, carPark.getLatitude(), 0.000001);
//        assertEquals(103.8541177, carPark.getLongitude(), 0.000001);
//        assertEquals("Free on Sundays and Public Holidays", carPark.getFreeParking());
//    }
//
//    @Test
//    void testParseCarParkAvailability() throws Exception {
//        // Arrange
//        String jsonResponse = "{\"items\":[{\"carpark_data\":[{\"carpark_number\":\"ACB\",\"carpark_info\":[{\"lots_available\":10}]}]}]}";
//        List<String> carParkNum = Arrays.asList("ACB", "ACM");
//
//        CarPark carPark1 = new CarPark("ACB", "BLK 270/271 ALBERT CENTRE BASEMENT CAR PARK", 1.301062605, 103.8541177, "BASEMENT CAR PARK", "ELECTRONIC PARKING", "Paid Parking");
//        when(carParkRepo.findById("ACB")).thenReturn(Optional.of(carPark1));
//
//        // Act
//        HashMap<Integer, List> result = CarParkService.parseCarParkAvailability(jsonResponse, carParkNum);
//
//        // Assert
//        assertEquals(1, result.size());
//        assertEquals(carPark1, result.get(0).get(0));
//        assertEquals(10, result.get(0).get(1));
//    }
//
//    @Test
//    void testGetCarParkAvailability_Success() throws Exception {
//        // Arrange
//        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
//        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("{\"items\":[{\"carpark_data\":[{\"carpark_number\":\"ACB\",\"carpark_info\":[{\"lots_available\":10}]}]}]}".getBytes()));
//
//        // Act
//        String result = CarParkService.getCarParkAvailability();
//
//        // Assert
//        assertNotNull(result);
//    }
//
////    @Test
////    void testGetCarParkAvailability_Error() throws Exception {
////        // Arrange
////        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);
////        // Act & Assert
////        assertEquals("Error in retrieving car park availability", carParkController.getCarParkAvailability());
////    }
//}
