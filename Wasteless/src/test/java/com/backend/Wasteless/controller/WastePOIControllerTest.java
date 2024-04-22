package com.backend.Wasteless.controller;

import com.backend.Wasteless.constants.WasteCategory;
import com.backend.Wasteless.model.CarPark;
import com.backend.Wasteless.model.WastePOI;
import com.backend.Wasteless.repository.CarParkRepository;
import com.backend.Wasteless.repository.WastePOIRepository;
import com.backend.Wasteless.service.WastePOIService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WastePOIControllerTest {
    private MockMvc mockMvc;

    @Mock
    private WastePOIRepository wastePOIRepository;
    @InjectMocks
    private WastePOIController wastePOIController;
    @Mock
    private WastePOIService wastePOIService;

    @Mock
    private CarParkRepository carParkRepository;

    @InjectMocks
    private CarParkController carParkController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(wastePOIController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addWastePOIDBEWasteSuccess() throws Exception {
        String category = "E_WASTE";
        String requestBody = "{\"category\": \"" + category + "\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addPOIData")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
    @Test
    void addWastePOIDBCashForTrashSuccess() throws Exception {
        String category = "CASH_FOR_TRASH";
        String requestBody = "{\"category\": \"" + category + "\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addPOIData")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

//    @Test
//    void addWastePOIDBNormalWasteSuccess() throws Exception {
//        String category = "NORMAL_WASTE";
//        String requestBody = "{\"category\": \"" + category + "\"}";
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addPOIData")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//    }
    @Test
    void addWastePOIDBLightingWasteSuccess() throws Exception {
        String category = "LIGHTING_WASTE";
        String requestBody = "{\"category\": \"" + category + "\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addPOIData")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
    @Test
    void addWastePOIDBWasteTreatmentSuccess() throws Exception {
        String category = "WASTE_TREATMENT";
        String requestBody = "{\"category\": \"" + category + "\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addPOIData")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void retrievePOIByCategorySuccess() throws Exception {

        // Mock data
        List<WastePOI> wastePOIs = Arrays.asList(
                new WastePOI("1", "POI1", WasteCategory.E_WASTE, Arrays.asList("CP1", "CP2"), 1.0, 1.0, 12345, "Address1", "Description1", "crc1", "2022-01-01"),
                new WastePOI("2", "POI2", WasteCategory.NORMAL_WASTE, Arrays.asList("CP3", "CP4"), 2.0, 2.0, 23456, "Address2", "Description2", "crc2", "2022-01-02")
        );

        when(wastePOIRepository.findAllByWasteCategory(WasteCategory.E_WASTE)).thenReturn(wastePOIs);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/retrievePOIByCategory")
                        .param("category", "E_WASTE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String wasteCategory = jsonNode.get(0).get("wasteCategory").asText();
        String expected = WasteCategory.E_WASTE.toString();
        System.out.println("responseBody: " + responseBody);
        assertEquals( expected, wasteCategory);
    }

    @Test
    void retrieveAllPOISuccess() throws Exception {

        // Mock data
        List<WastePOI> wastePOIs = Arrays.asList(
                new WastePOI("1", "POI1", WasteCategory.NORMAL_WASTE, Arrays.asList("CP1", "CP2"), 1.0, 1.0, 12345, "Address1", "Description1", "crc1", "2022-01-01"),
                new WastePOI("2", "POI2", WasteCategory.E_WASTE, Arrays.asList("CP3", "CP4"), 2.0, 2.0, 23456, "Address2", "Description2", "crc2", "2022-01-02"),
                new WastePOI("3", "POI3", WasteCategory.LIGHTING_WASTE, Arrays.asList("CP1", "CP2"), 1.0, 1.0, 12345, "Address1", "Description1", "crc1", "2022-01-01"),
                new WastePOI("4", "POI4", WasteCategory.WASTE_TREATMENT, Arrays.asList("CP1", "CP2"), 1.0, 1.0, 12345, "Address1", "Description1", "crc1", "2022-01-01"),
                new WastePOI("5", "POI5", WasteCategory.CASH_FOR_TRASH, Arrays.asList("CP1", "CP2"), 1.0, 1.0, 12345, "Address1", "Description1", "crc1", "2022-01-01")
                );

        when(wastePOIRepository.findAll()).thenReturn(wastePOIs);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/retrieveAllPOI")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<WasteCategory> expected = List.of(WasteCategory.values());
        System.out.println(expected);
        String responseBody = result.getResponse().getContentAsString();
        System.out.println(responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        List<String> wasteCategories = new ArrayList<>();

        for (JsonNode node : jsonNode) {
            String wasteCategory = node.get("wasteCategory").asText();
            wasteCategories.add(wasteCategory);
        }
        List<WasteCategory> actual = wasteCategories.stream()
                .map(WasteCategory::valueOf)
                .collect(Collectors.toList());

        assertEquals(new ArrayList<>(expected), actual);
    }

//    @Test
//    void retrieveNearestPOIsSuccess() throws Exception {
//        List<WastePOI> wastePOIs = Arrays.asList(
//                new WastePOI("1", "POI1", WasteCategory.NORMAL_WASTE, Arrays.asList("CP1", "CP2"), 1.0, 1.0, 12345, "Address1", "Description1", "crc1", "2022-01-01"),
//                new WastePOI("2", "POI2", WasteCategory.E_WASTE, Arrays.asList("CP3", "CP4"), 2.0, 2.0, 23456, "Address2", "Description2", "crc2", "2022-01-02")
//        );
//
//        when(wastePOIRepository.findAll()).thenReturn(wastePOIs);
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/retrieveNearestPOIs")
//                        .param("latitude", "1.0")
//                        .param("longitude", "1.0")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String responseBody = result.getResponse().getContentAsString();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        String poiName = jsonNode.get(0).get("poi_name").asText();
//        String expectedPoiName = "POI1";
//        System.out.println("responseBody: " + responseBody);
//        assertEquals(expectedPoiName, poiName);
//    }
}
