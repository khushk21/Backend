package com.backend.Wasteless.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

@Document
public class CarPark {

    @Id
    private String carParkNo;
    private String address;
    private double latitude;
    private double longitude;
    private String carParkType;
    private String parkingType;
    private String freeParking;

    public CarPark(String carParkNo, String address, double latitude, double longitude, String carParkType, String parkingType, String freeParking) {
        this.carParkNo = carParkNo;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.carParkType = carParkType;
        this.parkingType = parkingType;
        this.freeParking = freeParking;
    }
    @Override
    public int hashCode() {
        return Objects.hash(carParkNo, address, latitude, longitude, carParkType, parkingType, freeParking);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarPark carPark = (CarPark) o;
        return Double.compare(carPark.latitude, latitude) == 0 &&
                Double.compare(carPark.longitude, longitude) == 0 &&
                Objects.equals(carParkNo, carPark.carParkNo) &&
                Objects.equals(address, carPark.address) &&
                Objects.equals(carParkType, carPark.carParkType) &&
                Objects.equals(parkingType, carPark.parkingType) &&
                Objects.equals(freeParking, carPark.freeParking);
    }

    public String getCarParkNo() {
        return carParkNo;
    }

    public void setCarParkNo(String carParkNo) {
        this.carParkNo = carParkNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCarParkType() {
        return carParkType;
    }

    public void setCarParkType(String carParkType) {
        this.carParkType = carParkType;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getFreeParking() {
        return freeParking;
    }

    public void setFreeParking(String freeParking) {
        this.freeParking = freeParking;
    }
}
