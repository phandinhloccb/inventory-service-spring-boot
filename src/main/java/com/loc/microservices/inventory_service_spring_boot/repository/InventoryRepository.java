package com.loc.microservices.inventory_service_spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loc.microservices.inventory_service_spring_boot.model.Inventory;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    boolean existsBySkuCodeAndQuantityGreaterThanEqual(String skuCode, Integer quantity);
}