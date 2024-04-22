package com.backend.Wasteless.controller;

public class WasteTreatment implements FactoryDesign{
    @Override
    public void parent() {
        System.out.println("Inside WasteTreatement::parent() method.");
    }
}
