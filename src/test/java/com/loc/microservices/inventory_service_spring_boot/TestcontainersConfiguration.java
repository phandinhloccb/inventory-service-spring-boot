package com.loc.microservices.inventory_service_spring_boot;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@TestConfiguration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "tests.containers.enabled", havingValue = "true", matchIfMissing = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	MySQLContainer<?> mysqlContainer() {
		return new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
	}

}
