package com.backend.Wasteless.controller;

public class EWaste implements FactoryDesign {
    @Override
    public void parent() {
        System.out.println("Inside EWaste::parent() method.");
    }
}
