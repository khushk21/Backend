package com.backend.Wasteless.controller;

public class NormalWaste implements FactoryDesign{
    @Override
    public void parent() {
        System.out.println("Inside NormalWaste::parent() method.");
    }
}
