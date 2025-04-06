package com.loc.microservices.inventory_service_spring_boot;

import org.springframework.boot.SpringApplication;

public class TestInventoryServiceSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.from(InventoryServiceSpringBootApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
