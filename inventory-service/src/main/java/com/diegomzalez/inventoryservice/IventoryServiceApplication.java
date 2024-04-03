package com.diegomzalez.iventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.diegomzalez.iventoryservice.model.Inventory;
import com.diegomzalez.iventoryservice.repository.InventoryRepository;

@SpringBootApplication
public class IventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return (args) -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iphone_13");
			inventory.setQuantity(100);
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("iphone_14");
			inventory1.setQuantity(0);
			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}
}
