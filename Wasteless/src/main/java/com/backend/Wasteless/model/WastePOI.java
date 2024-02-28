package com.backend.Wasteless.model;

import com.backend.Wasteless.constants.WasteCategory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document
public class WastePOI {

    @Id
    private String id;
    private String POI_name;
    private WasteCategory wasteCategory;
    private List<String> nearbyCarPark = new ArrayList<>(5);
    private double latitude;
    private double longitude;
    private int POI_postal_code;
    private String address;
    private String POI_description;
    private String POI_inc_crc;
    private String POI_feml_upd_d;

    public WastePOI(String id, String POI_name, WasteCategory wasteCategory, List<String> nearbyCarPark, double latitude, double longitude, int POI_postal_code, String address, String POI_description, String POI_inc_crc, String POI_feml_upd_d) {
        this.id = id;
        this.POI_name = POI_name;
        this.wasteCategory = wasteCategory;
        this.nearbyCarPark = nearbyCarPark;
        this.latitude = latitude;
        this.longitude = longitude;
        this.POI_postal_code = POI_postal_code;
        this.address = address;
        this.POI_description = POI_description;
        this.POI_inc_crc = POI_inc_crc;
        this.POI_feml_upd_d = POI_feml_upd_d;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPOI_name() {
        return POI_name;
    }

    public void setPOI_name(String POI_name) {
        this.POI_name = POI_name;
    }

    public WasteCategory getWasteCategory() {
        return wasteCategory;
    }

    public void setWasteCategory(WasteCategory wasteCategory) {
        this.wasteCategory = wasteCategory;
    }

    public List<String> getNearbyCarPark() {
        return nearbyCarPark;
    }

    public void setNearbyCarPark(List<String> nearbyCarPark) {
        this.nearbyCarPark = nearbyCarPark;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPOI_postal_code() {
        return POI_postal_code;
    }

    public void setPOI_postal_code(int POI_postal_code) {
        this.POI_postal_code = POI_postal_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPOI_description() {
        return POI_description;
    }

    public void setPOI_description(String POI_description) {
        this.POI_description = POI_description;
    }

    public String getPOI_inc_crc() {
        return POI_inc_crc;
    }

    public void setPOI_inc_crc(String POI_inc_crc) {
        this.POI_inc_crc = POI_inc_crc;
    }

    public String getPOI_feml_upd_d() {
        return POI_feml_upd_d;
    }

    public void setPOI_feml_upd_d(String POI_feml_upd_d) {
        this.POI_feml_upd_d = POI_feml_upd_d;
    }
}
