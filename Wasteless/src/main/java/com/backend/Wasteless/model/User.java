package com.backend.Wasteless.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document
public class User implements UserInterface {
    @Id
    private String userName;

    private String name;

    private String email;

    private String password;

    private List <WastePOI> favourites = new ArrayList <WastePOI>(5);
    private List<WasteRecord> wasteRecords = new ArrayList<>();

    private int points;

    public User(String userName, String name, String email, String password) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public List<WasteRecord> getWasteRecords() {
        return wasteRecords;
    }

    @Override
    public void setWasteRecords(List<WasteRecord> wasteRecords) {
        this.wasteRecords = wasteRecords;
    }

    @Override
    public List<WastePOI> getFavourites() {
        return favourites;
    }

    @Override
    public void setFavourites(List<WastePOI> favourites) {
        this.favourites = favourites;
    }

}
