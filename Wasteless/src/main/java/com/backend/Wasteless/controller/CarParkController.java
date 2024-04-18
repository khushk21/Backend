package com.backend.Wasteless.controller;

import com.backend.Wasteless.service.CarParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class CarParkController {

    @Autowired
    private CarParkService carParkService;

    @GetMapping("/retrieveCarParkInfo")
    public HashMap<Integer, List> retrieveCarParkInfo(@RequestParam List<String> carParkNum) {
        return carParkService.retrieveCarParkInfo(carParkNum);
    }

    @PostMapping("/addCarParkData")
    public String addCarPark(@RequestBody Map<String, Object> payload) {
        return carParkService.addCarParkData(payload);
    }
}
