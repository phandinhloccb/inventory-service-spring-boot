package com.loc.inventory_service.application.service

import com.loc.inventory_service.application.port.InventoryRepositoryPort
import com.loc.inventory_service.domain.model.Inventory
import org.springframework.stereotype.Service

@Service
class CheckStockService(
    private val inventoryRepositoryPort: InventoryRepositoryPort
) {
    fun isInStock(inventory: Inventory): Boolean {
        return inventoryRepositoryPort.existsBySkuCodeAndQuantityGreaterThanEqual(inventory.skuCode,inventory.quantity)
    }
}
