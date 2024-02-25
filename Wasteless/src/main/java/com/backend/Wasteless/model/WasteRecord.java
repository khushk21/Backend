package com.backend.Wasteless.model;

import com.backend.Wasteless.constants.WasteCategory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document
public class WasteRecord {

    @Id
    private LocalDateTime dateTime;
    private WasteCategory category;
    private double weight;

    public WasteRecord(LocalDateTime dateTime, WasteCategory category, double weight) {
        this.dateTime = dateTime;
        this.category = category;
        this.weight = weight;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public WasteCategory getCategory() {
        return category;
    }

    public void setCategory(WasteCategory category) {
        this.category = category;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
