package com.loc.inventory_service.application.service

import com.loc.inventory_service.application.port.InventoryRepositoryPort
import com.loc.inventory_service.domain.model.Inventory
import org.springframework.stereotype.Service

@Service
class AddInventoryService(
    private val inventoryRepositoryPort: InventoryRepositoryPort
) {
    fun addInventory(inventory: Inventory): Inventory{
        return inventoryRepositoryPort.addInventory(inventory)
    }
}