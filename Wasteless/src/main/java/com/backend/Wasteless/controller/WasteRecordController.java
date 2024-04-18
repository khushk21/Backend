package com.backend.Wasteless.controller;

import com.backend.Wasteless.model.User;
import com.backend.Wasteless.service.WasteRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class WasteRecordController {

    @Autowired
    private WasteRecordService wasteRecordService;

    @PostMapping("/addRecord")
    public HashMap<String, Object> addNewRecord(@RequestBody Map<String, Object> payload) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            User user = wasteRecordService.addNewRecord(payload);
            result.put("user", user);
            result.put("error", null);
        } catch (IllegalArgumentException e) {
            result.put("user", null);
            result.put("error", new Error(e.getMessage()));
        }
        return result;
    }
}
