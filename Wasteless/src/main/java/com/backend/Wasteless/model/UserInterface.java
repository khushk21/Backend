package com.backend.Wasteless.model;

import java.util.List;

public interface UserInterface {
    String getPassword();

    void setPassword(String password);

    String getUserName();

    void setUserName(String userName);

    String getName();

    void setName(String name);

    String getEmail();

    void setEmail(String email);

    int getPoints();

    void setPoints(int points);

    List<WasteRecord> getWasteRecords();

    void setWasteRecords(List<WasteRecord> wasteRecords);

    List<WastePOI> getFavourites();

    void setFavourites(List<WastePOI> favourites);
}
