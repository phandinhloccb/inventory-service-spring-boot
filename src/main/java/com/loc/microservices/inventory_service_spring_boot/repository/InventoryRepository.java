package com.loc.microservices.inventory_service_spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loc.microservices.inventory_service_spring_boot.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    boolean existsBySkuCodeAndQuantityGreaterThanEqual(String skuCode, Integer quantity);
    
    Optional<Inventory> findBySkuCode(String skuCode);
}