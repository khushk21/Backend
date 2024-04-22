package com.backend.Wasteless.controller;

public class CashForTrash implements FactoryDesign{
    @Override
    public void parent() {
        System.out.println("Inside CashForTrash::parent() method.");
    }
}
