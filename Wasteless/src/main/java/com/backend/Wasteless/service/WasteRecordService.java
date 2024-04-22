package com.backend.Wasteless.service;

import com.backend.Wasteless.constants.WasteCategory;
import com.backend.Wasteless.model.User;
import com.backend.Wasteless.model.WasteRecord;
import com.backend.Wasteless.repository.UserRepository;
import com.backend.Wasteless.repository.WasteRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WasteRecordService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WasteRecordRepository wasteRecordRepo;

    public User addNewRecord(Map<String, Object> payload) {
        String username = (String) payload.get("username");
        Optional<User> optionalUser = userRepo.findById(username);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Incorrect Username provided.");
        }

        try {
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

//            updateRanking(user);

            if (user.getWasteRecords() == null) {
                user.setWasteRecords(new ArrayList<>(List.of(wasteRecord)));
            } else {
                List<WasteRecord> wasteRecordList = new ArrayList<>(user.getWasteRecords());
                wasteRecordList.add(wasteRecord);
                user.setWasteRecords(wasteRecordList);
            }

            return userRepo.save(user);
        } catch (DateTimeException | IllegalArgumentException e) {
            throw new IllegalArgumentException(e instanceof DateTimeException ? "Invalid timestamp provided." : "Invalid waste category provided.");
        }
    }

    public Integer calculatePoints(int weight, WasteCategory category) {
        int points = 0;
        switch (category) {
            case NORMAL_WASTE -> points += 7 * weight;
            case E_WASTE -> points += 3 * weight;
            case LIGHTING_WASTE -> points += 4 * weight;
            case WASTE_TREATMENT -> points += 6 * weight;
            case CASH_FOR_TRASH -> points += 9 * weight;
        }
        return points;
    }

//    private void updateRanking(User user) {
//        int points = user.getPoints();
//        if (points >= 1000) {
//            user.setRank("Gold");
//        } else if (points >= 500) {
//            user.setRank("Silver");
//        } else if (points >= 100) {
//            user.setRank("Bronze");
//        } else {
//            user.setRank("Newbie");
//        }
//    }
}
