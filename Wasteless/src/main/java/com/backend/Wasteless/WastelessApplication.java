package com.backend.Wasteless;

import com.backend.Wasteless.controller.FactoryDesign;
import com.backend.Wasteless.controller.WasteRecordFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.ArrayList;
import java.util.List;
// Product Interface


@SpringBootApplication
public class WastelessApplication {

	public static void main(String[] args) {

		SpringApplication.run(WastelessApplication.class, args);

		WasteRecordFactory wasteRecordFactory = new WasteRecordFactory();

		FactoryDesign waste1 = wasteRecordFactory.getWasteRecord("E_WASTE");
		waste1.parent();

		FactoryDesign waste2 = wasteRecordFactory.getWasteRecord("NORMAL_WASTE");
		waste2.parent();

		FactoryDesign waste3 = wasteRecordFactory.getWasteRecord("LIGHTING_WASTE");
		waste3.parent();

		FactoryDesign waste4 = wasteRecordFactory.getWasteRecord("WASTE_TREATMENT");
		waste4.parent();

		FactoryDesign waste5 = wasteRecordFactory.getWasteRecord("CASH_FOR_TRASH");
		waste5.parent();
	}

}
