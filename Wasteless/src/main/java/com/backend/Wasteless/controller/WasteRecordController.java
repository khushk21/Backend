package com.backend.Wasteless.controller;

import com.backend.Wasteless.constants.WasteCategory;
import com.backend.Wasteless.model.User;
import com.backend.Wasteless.model.WasteRecord;
import com.backend.Wasteless.repository.UserRepository;
import com.backend.Wasteless.repository.WasteRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.util.ObjectUtils.isEmpty;

@RestController
@CrossOrigin(origins="*")
public class WasteRecordController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private WasteRecordRepository wasteRecordRepo;

    @PostMapping("/addRecord")
    public HashMap<String, Object> addNewRecord(@RequestBody Map<String, Object> payload){
        String username = (String) payload.get("username");
        HashMap<String, Object> result = new HashMap<String, Object>();
        Optional<User> optionalUser = userRepo.findById(username);
        if(optionalUser.isEmpty()){
            result.put("user", null);
            result.put("error", new Error("Incorrect Username provided."));
        }
        else{
            try{
                String dateTimeString = (String) payload.get("dateTime");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
                String categoryString = (String) payload.get("category");
                WasteCategory category = WasteCategory.valueOf(categoryString);
                double weight = Double.parseDouble((String) payload.get("weight"));
                WasteRecord wasteRecord = new WasteRecord(dateTime, category, weight);
                User user = optionalUser.get();
                int points = user.getPoints();
                points += calculatePoints((int) wasteRecord.getWeight(), wasteRecord.getCategory());
                user.setPoints(points);
                if(user.getWasteRecords() == null){
                    user.setWasteRecords(new ArrayList<>(List.of(wasteRecord)));
                }
                else{
                    List<WasteRecord> wasteRecordList = new ArrayList<>(user.getWasteRecords());
                    wasteRecordList.add(wasteRecord);
                    user.setWasteRecords(wasteRecordList);
                }
                User savedUser = userRepo.save(user);
                if(isEmpty(savedUser)){
                    result.put("user", null);
                    result.put("error", new Error("Error in saving user records."));
                }
                else{
                    result.put("user", user);
                    result.put("error", null);
                }
            } catch (DateTimeException | IllegalArgumentException e){
                if(e instanceof DateTimeException){
                    result.put("error", new Error("Invalid timestamp provided."));
                }
                else{
                    result.put("error", new Error("Invalid waste category provided."));
                }
                result.put("user", null);
            }
        }
        return result;
    }

    public Integer calculatePoints(int weight, WasteCategory category){
        int points = 0;
        if(category.equals(WasteCategory.NORMAL_WASTE)){
            points +=  7 * weight;
        }
        else if(category.equals(WasteCategory.E_WASTE)){
            points += 3 * weight;
        }
        else if(category.equals(WasteCategory.LIGHTING_WASTE)){
            points += 4 * weight;
        }
        else if(category.equals(WasteCategory.WASTE_TREATMENT)){
            points += 6 * weight;
        }
        else if(category.equals(WasteCategory.CASH_FOR_TRASH)){
            points += 9 * weight;
        }
        return points;
    }
}
