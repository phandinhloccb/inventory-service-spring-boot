package com.loc.microservices.inventory_service_spring_boot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class InventoryServiceSpringBootApplicationTests {

	@Test
	void contextLoads() {
	}

}
