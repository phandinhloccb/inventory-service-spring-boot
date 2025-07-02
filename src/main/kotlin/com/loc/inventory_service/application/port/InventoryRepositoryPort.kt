package com.loc.inventory_service.application.port

import com.loc.inventory_service.domain.model.Inventory

interface InventoryRepositoryPort {
    fun existsBySkuCodeAndQuantityGreaterThanEqual(skuCode: String, quantity: Int): Boolean
    fun addInventory(inventory: Inventory): Inventory
}