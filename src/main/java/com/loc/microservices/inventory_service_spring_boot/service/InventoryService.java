package com.loc.microservices.inventory_service_spring_boot.service;

import org.springframework.stereotype.Service;

import com.loc.microservices.inventory_service_spring_boot.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock( String skuCode, Integer quantity) {
        return inventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, quantity);
    }
    
}

