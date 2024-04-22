package com.backend.Wasteless.controller;

public class WasteRecordFactory {
    //use getCar method to get object of type shape
    public FactoryDesign getWasteRecord(String getWasteRecord){
        if(getWasteRecord == null) {
            return null;
        }
        if(getWasteRecord.equalsIgnoreCase("E_WASTE")){
            return new EWaste();
        } else if(getWasteRecord.equalsIgnoreCase("NORMAL_WASTE")){
            return new NormalWaste();
        } else if(getWasteRecord.equalsIgnoreCase("LIGHTING_WASTE")){
            return new LightingWaste();
        } else if(getWasteRecord.equalsIgnoreCase("WASTE_TREATMENT")){
            return new WasteTreatment();
        } else if(getWasteRecord.equalsIgnoreCase("CASH_FOR_TRASH")){
            return new CashForTrash();
        }
        return null;
    }
}
