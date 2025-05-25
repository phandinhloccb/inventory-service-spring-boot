package com.loc.microservices.inventory_service_spring_boot.graphql;

import java.util.List;
import java.util.Optional;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.loc.microservices.inventory_service_spring_boot.model.Inventory;
import com.loc.microservices.inventory_service_spring_boot.repository.InventoryRepository;
import com.loc.microservices.inventory_service_spring_boot.service.InventoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InventoryResolver {

    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;

    @QueryMapping
    public boolean isInStock(@Argument String skuCode, @Argument Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }
    
    @QueryMapping
    public Optional<Inventory> inventory(@Argument Long id) {
        return inventoryRepository.findById(id);
    }
    
    @QueryMapping
    public List<Inventory> inventories() {
        return inventoryRepository.findAll();
    }
} 