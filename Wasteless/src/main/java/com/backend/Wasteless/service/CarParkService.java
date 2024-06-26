package com.backend.Wasteless.service;

import com.backend.Wasteless.model.CarPark;
import com.backend.Wasteless.repository.CarParkRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class CarParkService {

    private static final Logger logger = LoggerFactory.getLogger(CarParkService.class);

    @Autowired
    private CarParkRepository carParkRepo;

    public HashMap<Integer, List> retrieveCarParkInfo(List<String> carParkNum) {
        HashMap<Integer, List> carParkInfo = new HashMap<>();
        List<CarPark> carParks = carParkRepo.findAllById(carParkNum);
        try {
            String jsonResponse = getCarParkAvailability();
            if (jsonResponse.equals("Error in retrieving car park availability")) {
                return null;
            } else {
                carParkInfo = parseCarParkAvailability(jsonResponse, carParkNum);
                return carParkInfo;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String addCarParkData(Map<String, Object> payload) {
        String request = (String) payload.get("request");
        String result = "";
        if (request.equals("createCarParkDB")) {
            try {
                File carParkCSV = new File("src/main/java/com/backend/Wasteless/assets/data/hdb-carpark-information.csv");
                BufferedReader reader = new BufferedReader(new FileReader(carParkCSV));
                reader.readLine();
                String line;
                while ((line = reader.readLine()) != null) {
                    CarPark carPark = getCarPark(line);
                    carParkRepo.save(carPark);
                }
                result = "Car Park Database created successfully";
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error in creating Car Park Database";
            }
        } else {
            result = "Invalid request";
        }
        return result;
    }

    public CarPark getCarPark(String line) {
        line = line.replaceAll("\"", "");
        String[] data = line.split(",");
        String carParkNo = data[0];
        String address = data[1];
        String carParkType = data[4];
        String parkingType = data[5];
        double latitude = Double.parseDouble(data[data.length - 2]);
        double longitude = Double.parseDouble(data[data.length - 1]);
        String freeParking = data[7].equals("NO") ? "Paid Parking" : "Free on Sundays and Public Holidays";

        logger.info("Testing" + carParkNo + address + latitude + longitude + carParkType + parkingType + freeParking);
        return new CarPark(carParkNo, address, latitude, longitude, carParkType, parkingType, freeParking);
    }

    public HashMap<Integer, List> parseCarParkAvailability(String jsonResponse, List<String> carParkNum) {
        HashMap<Integer, List> carParkInfo = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            JsonNode items = root.get("items");
            JsonNode carParkData = items.get(0).get("carpark_data");
            int count = 0;
            for (JsonNode carPark : carParkData) {
                String carParkNo = carPark.get("carpark_number").asText();
                if (carParkNum.contains(carParkNo)) {
                    int carParkLots = carPark.get("carpark_info").get(0).get("lots_available").asInt();
                    logger.info("Testing " + carParkNo);
                    CarPark carParkObj = carParkRepo.findById(carParkNo).isPresent() ? carParkRepo.findById(carParkNo).get() : null;
                    if (carParkObj != null) {
                        carParkInfo.put(count, List.of(carParkObj, carParkLots));
                        count++;
                    }
                }
            }
            return carParkInfo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCarParkAvailability() throws IOException {
        URL url = new URL("https://api.data.gov.sg/v1/transport/carpark-availability");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();
            return responseBuilder.toString();
        } else {
            return "Error in retrieving car park availability";
        }
    }
}
