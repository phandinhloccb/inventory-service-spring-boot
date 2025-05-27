package com.loc.microservices.inventory_service_spring_boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.loc.microservices.inventory_service_spring_boot.dto.InventoryRequest;
import com.loc.microservices.inventory_service_spring_boot.model.Inventory;
import com.loc.microservices.inventory_service_spring_boot.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
        return inventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, quantity);
    }
    
    public Inventory createInventory(InventoryRequest request) {
        // Kiểm tra xem sản phẩm đã tồn tại chưa
        Optional<Inventory> existingInventory = inventoryRepository.findBySkuCode(request.getSkuCode());
        
        if (existingInventory.isPresent()) {
            // Nếu đã tồn tại, cập nhật số lượng
            Inventory inventory = existingInventory.get();
            inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
            return inventoryRepository.save(inventory);
        } else {
            // Nếu chưa tồn tại, tạo mới
            Inventory inventory = new Inventory();
            inventory.setSkuCode(request.getSkuCode());
            inventory.setQuantity(request.getQuantity());
            return inventoryRepository.save(inventory);
        }
    }
    
    public List<Inventory> createMultipleInventories(List<InventoryRequest> requests) {
        return requests.stream()
                .map(this::createInventory)
                .toList();
    }
    
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }
    
    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }
    
    public Inventory updateInventory(Long id, InventoryRequest request) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(id);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            inventory.setSkuCode(request.getSkuCode());
            inventory.setQuantity(request.getQuantity());
            return inventoryRepository.save(inventory);
        }
        throw new RuntimeException("Inventory not found with id: " + id);
    }
    
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}

