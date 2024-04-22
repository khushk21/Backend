package com.backend.Wasteless.service;

import com.backend.Wasteless.constants.WasteCategory;
import com.backend.Wasteless.model.CarPark;
import com.backend.Wasteless.model.WastePOI;
import com.backend.Wasteless.repository.CarParkRepository;
import com.backend.Wasteless.repository.WastePOIRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WastePOIService {

    @Autowired
    private WastePOIRepository wastePOIRepo;

    @Autowired
    private CarParkRepository carParkRepo;

    public String createEWasteDB() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File geoJsonFile = new File("src/main/java/com/backend/Wasteless/assets/data/e-waste-recycling-geojson.geojson");
            JsonNode jsonNode = objectMapper.readTree(geoJsonFile);
            if(jsonNode.has("type") && jsonNode.get("type").asText().equals("FeatureCollection")){
                JsonNode features = jsonNode.get("features");
                int count = 0;
                for(JsonNode feature: features){
                    JsonNode properties = feature.get("properties");
                    double latitude = 0.0;
                    double longitude = 0.0;
                    JsonNode geometry = feature.get("geometry");
                    latitude = geometry.get("coordinates").get(1).asDouble();
                    longitude = geometry.get("coordinates").get(0).asDouble();
                    List<String> nearbyCarPark = getTop5NearbyCarParks(latitude, longitude);
                    if(!properties.isEmpty()){
                        String rawDescription = properties.get("Description").asText();
                        Document doc = Jsoup.parse(rawDescription);
                        Elements attributeRows = doc.select("tr:has(th:matchesOwn(^LANDYADDRESSPOINT$|^LANDXADDRESSPOINT$|^PHOTOURL$|^HYPERLINK$|^DESCRIPTION$|^ADDRESSUNITNUMBER$|^ADDRESSSTREETNAME$|^ADDRESSPOSTALCODE$|^ADDRESSFLOORNUMBER$|^ADDRESSBUILDINGNAME$|^ADDRESSBLOCKNUMBER$|^INC_CRC$|^FMEL_UPD_D$|^NAME$))");
                        String address = "";
                        String addressUnitNo = "";
                        String addressStreetName = "";
                        String addressFloorNo = "";
                        String addressBuildingName = "";
                        String addressBlockNo = "";
                        String id = "E_WASTE_" + count;
                        String POI_name = "";
                        String POI_inc_crc = "";
                        String POI_feml_upd_d = "";
                        int POI_postal_code = 0;
                        String POI_description = "";
                        count++;
                        // Iterate through attribute rows and extract values
                        for (Element row : attributeRows) {
                            String attributeName = row.select("th").text();
                            String attributeValue = row.select("td").text();
                            switch (attributeName) {
                                case "DESCRIPTION" -> POI_description = attributeValue;
                                case "ADDRESSUNITNUMBER" -> addressUnitNo = attributeValue;
                                case "ADDRESSSTREETNAME" -> addressStreetName = attributeValue;
                                case "ADDRESSFLOORNUMBER" -> addressFloorNo = attributeValue;
                                case "ADDRESSBUILDINGNAME" -> addressBuildingName = attributeValue;
                                case "ADDRESSBLOCKNUMBER" -> addressBlockNo = attributeValue;
                                case "INC_CRC" -> POI_inc_crc = attributeValue;
                                case "FMEL_UPD_D" -> POI_feml_upd_d = attributeValue;
                                case "NAME" -> POI_name = attributeValue;
                                case "ADDRESSPOSTALCODE" -> POI_postal_code = Integer.parseInt(attributeValue);
                            }
                        }
                        address = addressUnitNo + " " + addressFloorNo + " " + addressBlockNo + " " + addressBuildingName + " " + addressStreetName;
                        WastePOI wastePOI = new WastePOI(id, POI_name, WasteCategory.E_WASTE, nearbyCarPark, latitude, longitude, POI_postal_code, address, POI_description, POI_inc_crc, POI_feml_upd_d);
                        wastePOIRepo.save(wastePOI);
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            return "Error in reading file";
        }
        return "E-Waste Database created successfully";

    }

    public String createCashForTrashDB(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File geoJsonFile = new File("src/main/java/com/backend/Wasteless/assets/data/cash-for-trash-geojson.geojson");
            JsonNode jsonNode = objectMapper.readTree(geoJsonFile);
            if(jsonNode.has("type") && jsonNode.get("type").asText().equals("FeatureCollection")){
                JsonNode features = jsonNode.get("features");
                int count = 0;
                for(JsonNode feature: features){
                    JsonNode properties = feature.get("properties");
                    double latitude = 0.0;
                    double longitude = 0.0;
                    JsonNode geometry = feature.get("geometry");
                    latitude = geometry.get("coordinates").get(1).asDouble();
                    longitude = geometry.get("coordinates").get(0).asDouble();
                    List<String> nearbyCarPark = getTop5NearbyCarParks(latitude, longitude);
                    if(!properties.isEmpty()){
                        String rawDescription = properties.get("Description").asText();
                        Document doc = Jsoup.parse(rawDescription);
                        Elements attributeRows = doc.select("tr:has(th:matchesOwn(^LANDYADDRESSPOINT$|^LANDXADDRESSPOINT$|^PHOTOURL$|^HYPERLINK$|^DESCRIPTION$|^ADDRESSUNITNUMBER$|^ADDRESSSTREETNAME$|^ADDRESSPOSTALCODE$|^ADDRESSFLOORNUMBER$|^ADDRESSBUILDINGNAME$|^ADDRESSBLOCKNUMBER$|^INC_CRC$|^FMEL_UPD_D$|^NAME$))");
                        String address = "";
                        String addressUnitNo = "";
                        String addressStreetName = "";
                        String addressFloorNo = "";
                        String addressBuildingName = "";
                        String addressBlockNo = "";
                        String id = "CASH_FOR_TRASH_" + count;
                        String POI_name = "";
                        String POI_inc_crc = "";
                        String POI_feml_upd_d = "";
                        int POI_postal_code = 0;
                        String POI_description = "";
                        count++;
                        // Iterate through attribute rows and extract values
                        for (Element row : attributeRows) {
                            String attributeName = row.select("th").text();
                            String attributeValue = row.select("td").text();
                            switch (attributeName) {
                                case "DESCRIPTION" -> POI_description = attributeValue;
                                case "ADDRESSUNITNUMBER" -> addressUnitNo = attributeValue;
                                case "ADDRESSSTREETNAME" -> addressStreetName = attributeValue;
                                case "ADDRESSFLOORNUMBER" -> addressFloorNo = attributeValue;
                                case "ADDRESSBUILDINGNAME" -> addressBuildingName = attributeValue;
                                case "ADDRESSBLOCKNUMBER" -> addressBlockNo = attributeValue;
                                case "INC_CRC" -> POI_inc_crc = attributeValue;
                                case "FMEL_UPD_D" -> POI_feml_upd_d = attributeValue;
                                case "NAME" -> POI_name = attributeValue;
                                case "ADDRESSPOSTALCODE" -> POI_postal_code = Integer.parseInt(attributeValue);
                            }
                        }
                        address = addressUnitNo + " " + addressFloorNo + " " + addressBlockNo + " " + addressBuildingName + " " + addressStreetName;
                        WastePOI wastePOI = new WastePOI(id, POI_name, WasteCategory.CASH_FOR_TRASH, nearbyCarPark, latitude, longitude, POI_postal_code, address, POI_description, POI_inc_crc, POI_feml_upd_d);
                        wastePOIRepo.save(wastePOI);
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            return "Error in reading file";
        }
        return "Cash For Trash Database created successfully";
    }

    public String createNormalWasteDB(){
        String API_KEY = "API_KEY";
        File carParkCSV = new File("src/main/java/com/backend/Wasteless/assets/data/listing-of-general-waste-collectors.csv");
        try (FileReader fileReader = new FileReader(carParkCSV);
             CSVParser csvParser = CSVFormat.DEFAULT
                     .withHeader("company_name","company_address","telephone_no","fax_no,class_of_licence")
                     .withSkipHeaderRecord()
                     .withQuote('"').withEscape('\\').parse(fileReader)) {
            WasteCategory category = WasteCategory.NORMAL_WASTE;
            int count = 0;
            for(CSVRecord record: csvParser){
                String name = record.get("company_name");
                String completeAddress = record.get("company_address");
                int postalCode = Integer.parseInt(completeAddress.substring(completeAddress.length() - 6).trim());
                List<Double> latlng = getLatLng(completeAddress, API_KEY);
                double latitude = latlng.get(0);
                double longitude = latlng.get(1);
                System.out.println("lat: " + latitude + " long: " + longitude);
                String id = "NORMAL_WASTE_" + count;
                count++;
                List<String> nearbyCarParks = getTop5NearbyCarParks(latitude, longitude);
                WastePOI wastePOI = new WastePOI(id, name, category, nearbyCarParks, latitude, longitude, postalCode, completeAddress, "Normal Waste Disposal", "No inc_crc", "No feml_upd_d");
                wastePOIRepo.save(wastePOI);
            }
            return "Normal Waste Database created successfully";
        }catch (IOException e) {
            e.printStackTrace();
            return "Error in creating Normal Waste Database";
        }
    }

    public String createLightingWasteDB(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File geoJsonFile = new File("src/main/java/com/backend/Wasteless/assets/data/lighting-waste-collection-points-geojson.geojson");
            JsonNode jsonNode = objectMapper.readTree(geoJsonFile);
            if(jsonNode.has("type") && jsonNode.get("type").asText().equals("FeatureCollection")){
                JsonNode features = jsonNode.get("features");
                int count = 0;
                for(JsonNode feature: features){
                    JsonNode properties = feature.get("properties");
                    double latitude = 0.0;
                    double longitude = 0.0;
                    JsonNode geometry = feature.get("geometry");
                    latitude = geometry.get("coordinates").get(1).asDouble();
                    longitude = geometry.get("coordinates").get(0).asDouble();
                    List<String> nearbyCarPark = getTop5NearbyCarParks(latitude, longitude);
                    if(!properties.isEmpty()){
                        String rawDescription = properties.get("Description").asText();
                        Document doc = Jsoup.parse(rawDescription);
                        Elements attributeRows = doc.select("tr:has(th:matchesOwn(^LANDYADDRESSPOINT$|^LANDXADDRESSPOINT$|^PHOTOURL$|^HYPERLINK$|^DESCRIPTION$|^ADDRESSUNITNUMBER$|^ADDRESSSTREETNAME$|^ADDRESSPOSTALCODE$|^ADDRESSFLOORNUMBER$|^ADDRESSBUILDINGNAME$|^ADDRESSBLOCKNUMBER$|^INC_CRC$|^FMEL_UPD_D$|^NAME$))");
                        String address = "";
                        String addressUnitNo = "";
                        String addressStreetName = "";
                        String addressFloorNo = "";
                        String addressBuildingName = "";
                        String addressBlockNo = "";
                        String id = "LIGHTING_WASTE_" + count;
                        String POI_name = "";
                        String POI_inc_crc = "";
                        String POI_feml_upd_d = "";
                        int POI_postal_code = 0;
                        String POI_description = "";
                        count++;
                        // Iterate through attribute rows and extract values
                        for (Element row : attributeRows) {
                            String attributeName = row.select("th").text();
                            String attributeValue = row.select("td").text();
                            switch (attributeName) {
                                case "DESCRIPTION" -> POI_description = attributeValue;
                                case "ADDRESSUNITNUMBER" -> addressUnitNo = attributeValue;
                                case "ADDRESSSTREETNAME" -> addressStreetName = attributeValue;
                                case "ADDRESSFLOORNUMBER" -> addressFloorNo = attributeValue;
                                case "ADDRESSBUILDINGNAME" -> addressBuildingName = attributeValue;
                                case "ADDRESSBLOCKNUMBER" -> addressBlockNo = attributeValue;
                                case "INC_CRC" -> POI_inc_crc = attributeValue;
                                case "FMEL_UPD_D" -> POI_feml_upd_d = attributeValue;
                                case "NAME" -> POI_name = attributeValue;
                                case "ADDRESSPOSTALCODE" -> POI_postal_code = attributeValue.isEmpty() ? -1 : Integer.parseInt(attributeValue);
                            }
                        }
                        address = addressUnitNo + " " + addressFloorNo + " " + addressBlockNo + " " + addressBuildingName + " " + addressStreetName;
                        WastePOI wastePOI = new WastePOI(id, POI_name, WasteCategory.LIGHTING_WASTE, nearbyCarPark, latitude, longitude, POI_postal_code, address, POI_description, POI_inc_crc, POI_feml_upd_d);
                        wastePOIRepo.save(wastePOI);
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            return "Error in reading file";
        }
        return "Lighting Waste Database created successfully";
    }

    public String createWasteTreatmentDB(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File geoJsonFile = new File("src/main/java/com/backend/Wasteless/assets/data/waste-treatment-geojson.geojson");
            JsonNode jsonNode = objectMapper.readTree(geoJsonFile);
            if(jsonNode.has("type") && jsonNode.get("type").asText().equals("FeatureCollection")){
                JsonNode features = jsonNode.get("features");
                int count = 0;
                for(JsonNode feature: features){
                    JsonNode properties = feature.get("properties");
                    double latitude = 0.0;
                    double longitude = 0.0;
                    JsonNode geometry = feature.get("geometry");
                    latitude = geometry.get("coordinates").get(1).asDouble();
                    longitude = geometry.get("coordinates").get(0).asDouble();
                    List<String> nearbyCarPark = getTop5NearbyCarParks(latitude, longitude);
                    if(!properties.isEmpty()){
                        String rawDescription = properties.get("Description").asText();
                        Document doc = Jsoup.parse(rawDescription);
                        Elements attributeRows = doc.select("tr:has(th:matchesOwn(^LANDYADDRESSPOINT$|^LANDXADDRESSPOINT$|^PHOTOURL$|^HYPERLINK$|^DESCRIPTION$|^ADDRESSUNITNUMBER$|^ADDRESSSTREETNAME$|^ADDRESSPOSTALCODE$|^ADDRESSFLOORNUMBER$|^ADDRESSBUILDINGNAME$|^ADDRESSBLOCKNUMBER$|^INC_CRC$|^FMEL_UPD_D$|^NAME$))");
                        String address = "";
                        String addressUnitNo = "";
                        String addressStreetName = "";
                        String addressFloorNo = "";
                        String addressBuildingName = "";
                        String addressBlockNo = "";
                        String id = "WASTE_TREATMENT_" + count;
                        String POI_name = "";
                        String POI_inc_crc = "";
                        String POI_feml_upd_d = "";
                        int POI_postal_code = 0;
                        String POI_description = "";
                        count++;
                        // Iterate through attribute rows and extract values
                        for (Element row : attributeRows) {
                            String attributeName = row.select("th").text();
                            String attributeValue = row.select("td").text();
                            switch (attributeName) {
                                case "DESCRIPTION" -> POI_description = attributeValue;
                                case "ADDRESSUNITNUMBER" -> addressUnitNo = attributeValue;
                                case "ADDRESSSTREETNAME" -> addressStreetName = attributeValue;
                                case "ADDRESSFLOORNUMBER" -> addressFloorNo = attributeValue;
                                case "ADDRESSBUILDINGNAME" -> addressBuildingName = attributeValue;
                                case "ADDRESSBLOCKNUMBER" -> addressBlockNo = attributeValue;
                                case "INC_CRC" -> POI_inc_crc = attributeValue;
                                case "FMEL_UPD_D" -> POI_feml_upd_d = attributeValue;
                                case "NAME" -> POI_name = attributeValue;
                                case "ADDRESSPOSTALCODE" -> POI_postal_code = attributeValue.isEmpty() ? -1 : Integer.parseInt(attributeValue);
                            }
                        }
                        address = addressUnitNo + " " + addressFloorNo + " " + addressBlockNo + " " + addressBuildingName + " " + addressStreetName;
                        WastePOI wastePOI = new WastePOI(id, POI_name, WasteCategory.WASTE_TREATMENT, nearbyCarPark, latitude, longitude, POI_postal_code, address, POI_description, POI_inc_crc, POI_feml_upd_d);
                        wastePOIRepo.save(wastePOI);
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            return "Error in reading file";
        }
        return "Waste Treatment Database created successfully";
    }

    public List<String> getTop5NearbyCarParks(double latitude, double longitude){
        List<CarPark> carParks = carParkRepo.findAll();
        HashMap<String, Double> distances = new HashMap<>();
        for(CarPark carPark: carParks){
            double distance = haversine(latitude, longitude, carPark.getLatitude(), carPark.getLongitude());
            distances.put(carPark.getCarParkNo(), distance);
        }
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(distances.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        List<String> top5CarParks = new ArrayList<>();
        int count = 1;
        for (Map.Entry<String, Double> entry : entryList) {
            if(count > 5){
                break;
            }
            top5CarParks.add(entry.getKey());
            count++;
        }
        return top5CarParks;
    }

    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
    public List<Double> getLatLng(String address, String API_KEY){
        try{
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + API_KEY;
            URI uri = URI.create(url);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(uri).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            Pattern pattern = Pattern.compile("\"location\"\\s*:\\s*\\{\\s*\"lat\"\\s*:\\s*(-?\\d+\\.\\d+),\\s*\"lng\"\\s*:\\s*(-?\\d+\\.\\d+)\\s*}");
            Matcher matcher = pattern.matcher(responseBody);
            List<Double> latLng = new ArrayList<>();
            if(matcher.find()){
                double latitude = Double.parseDouble(matcher.group(1));
                double longitude = Double.parseDouble(matcher.group(2));
                latLng.add(latitude);
                latLng.add(longitude);
            }
            return latLng;
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    // Helper methods and other logic can also be moved to this service class
}
