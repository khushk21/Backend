package com.backend.Wasteless.controller;

public class LightingWaste implements FactoryDesign{
    @Override
    public void parent() {
        System.out.println("Inside LightingWaste::parent() method.");
    }
}
