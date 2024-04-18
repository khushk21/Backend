package com.backend.Wasteless.controller;

import com.backend.Wasteless.model.CarPark;
import com.backend.Wasteless.service.WastePOIService;
import com.backend.Wasteless.model.WastePOI;
import com.backend.Wasteless.repository.CarParkRepository;
import com.backend.Wasteless.repository.UserRepository;
import com.backend.Wasteless.repository.WastePOIRepository;
import com.backend.Wasteless.constants.WasteCategory;
import com.backend.Wasteless.repository.WasteRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@RestController
@CrossOrigin(origins="*")
public class WastePOIController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private WasteRecordRepository wasteRecordRepo;
    @Autowired
    private WastePOIRepository wastePOIRepo;
    @Autowired
    private CarParkRepository carParkRepo;
    @Autowired
    private WastePOIService wastePOIService;

    @PostMapping("/addPOIData")
    public String addWastePOIDB(@RequestBody Map<String, Object> payload){
        String categoryString = (String) payload.get("category");
        WasteCategory wasteCat = WasteCategory.valueOf(categoryString);
        String result = "";
        if(wasteCat == WasteCategory.E_WASTE){
            result = wastePOIService.createEWasteDB();
        }
        else if(wasteCat == WasteCategory.CASH_FOR_TRASH){
            result = wastePOIService.createCashForTrashDB();
        }
        else if(wasteCat == WasteCategory.NORMAL_WASTE){
            result = wastePOIService.createNormalWasteDB();
        }
        else if(wasteCat == WasteCategory.LIGHTING_WASTE){
            result = wastePOIService.createLightingWasteDB();
        }
        else if(wasteCat == WasteCategory.WASTE_TREATMENT){
            result = wastePOIService.createWasteTreatmentDB();
        }
        return result;
    }

    //TODO: WRITE TEST CASES FOR THIS API
    @GetMapping("/retrievePOIByCategory")
    public List<WastePOI> retrievePOIByCategory(@RequestParam String category){
        WasteCategory wasteCategory = WasteCategory.valueOf(category);
        return wastePOIRepo.findAllByWasteCategory(wasteCategory);
    }

    //TODO: WRITE TEST CASES FOR THIS API
    @GetMapping("/retrieveAllPOI")
    public List<WastePOI> retrieveAllPOI(){
        return wastePOIRepo.findAll();
    }

    @GetMapping("/getPOIById")
    public WastePOI retrieveWastePOI(@RequestParam String id){
        return wastePOIRepo.findById(id).orElse(null);
    }

    //TODO: WRITE TEST CASES FOR THIS API
    @GetMapping("/retrieveNearestPOIs")
    public List<WastePOI> retrieveNearestPOIs(@RequestParam double latitude, @RequestParam double longitude, @RequestParam String category){
        WasteCategory wasteCategory = WasteCategory.valueOf(category);
        List<WastePOI> wastePOIs = wastePOIRepo.findAllByWasteCategory(wasteCategory);
        HashMap<WastePOI, Double> distances = new HashMap<>();
        for(WastePOI wastePOI: wastePOIs){
            double distance = wastePOIService.haversine(latitude, longitude, wastePOI.getLatitude(), wastePOI.getLongitude());
            distances.put(wastePOI, distance);
        }
        List<Map.Entry<WastePOI, Double>> entryList = new ArrayList<>(distances.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        List<WastePOI> nearestPOIs = new ArrayList<>();
        int count = 1;
        for (Map.Entry<WastePOI, Double> entry : entryList) {
            if(count > 5){
                break;
            }
            nearestPOIs.add(entry.getKey());
            count++;
        }
        return nearestPOIs;
    }
}
