package com.loc.microservices.inventory_service_spring_boot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loc.microservices.inventory_service_spring_boot.dto.InventoryRequest;
import com.loc.microservices.inventory_service_spring_boot.model.Inventory;
import com.loc.microservices.inventory_service_spring_boot.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Kiểm tra stock (endpoint đơn giản cho order service)
    @GetMapping("/simple")
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }
    
    // Tạo inventory mới
    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody InventoryRequest request) {
        Inventory inventory = inventoryService.createInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }
    
    // Tạo nhiều inventory cùng lúc
    @PostMapping("/bulk")
    public ResponseEntity<List<Inventory>> createMultipleInventories(@RequestBody List<InventoryRequest> requests) {
        List<Inventory> inventories = inventoryService.createMultipleInventories(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventories);
    }

    // Lấy tất cả inventory
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories() {
        List<Inventory> inventories = inventoryService.getAllInventories();
        return ResponseEntity.ok(inventories);
    }

    // Lấy inventory theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Optional<Inventory> inventory = inventoryService.getInventoryById(id);
        return inventory.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    // Cập nhật inventory
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody InventoryRequest request) {
        try {
            Inventory inventory = inventoryService.updateInventory(id, request);
            return ResponseEntity.ok(inventory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Xóa inventory
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}
