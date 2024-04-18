package com.backend.Wasteless.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document
public class User {
    @Id
    private String userName;
    private String name;
    private String email;
    private String password;
    private List<WastePOI> favourites;
    private List<WasteRecord> wasteRecords;
    private int points;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<WastePOI> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<WastePOI> favourites) {
        this.favourites = favourites;
    }

    public List<WasteRecord> getWasteRecords() {
        return wasteRecords;
    }

    public void setWasteRecords(List<WasteRecord> wasteRecords) {
        this.wasteRecords = wasteRecords;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }



    private User(Builder builder) {
        this.userName = builder.userName;
        this.name = builder.name;
        this.email = builder.email;
        this.password = builder.password;
        this.favourites = builder.favourites;
        this.wasteRecords = builder.wasteRecords;
        this.points = builder.points;
    }

    public static class Builder {
        private String userName;
        private String name;
        private String email;
        private String password;
        private List<WastePOI> favourites = new ArrayList<>();
        private List<WasteRecord> wasteRecords = new ArrayList<>();
        private int points;

        public Builder(String userName, String password, String name, String email) {
            this.userName = userName;
            this.password = password;
            this.email = email;
            this.name = name;
        }

//        public Builder withName(String name) {
//            this.name = name;
//            return this;
//        }
//
//        public Builder withEmail(String email) {
//            this.email = email;
//            return this;
//        }

        public Builder withFavourites(List<WastePOI> favourites) {
            this.favourites = favourites;
            return this;
        }

        public Builder withWasteRecords(List<WasteRecord> wasteRecords) {
            this.wasteRecords = wasteRecords;
            return this;
        }

        public Builder withPoints(int points) {
            this.points = points;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

//@Document
//public class User {
//    @Id
//    private String userName;
//
//    private String name;
//
//    private String email;
//
//    private String password;
//
//    private List <WastePOI> favourites = new ArrayList <WastePOI>(5);
//    private List<WasteRecord> wasteRecords = new ArrayList<>();
//
//    private int points;
//
//    public User(String userName, String name, String email, String password) {
//        this.userName = userName;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public int getPoints() {
//        return points;
//    }
//
//    public void setPoints(int points) {
//        this.points = points;
//    }
//
//    public List<WasteRecord> getWasteRecords() {
//        return wasteRecords;
//    }
//
//    public void setWasteRecords(List<WasteRecord> wasteRecords) {
//        this.wasteRecords = wasteRecords;
//    }
//
//    public List<WastePOI> getFavourites() {
//        return favourites;
//    }
//
//    public void setFavourites(List<WastePOI> favourites) {
//        this.favourites = favourites;
//    }


