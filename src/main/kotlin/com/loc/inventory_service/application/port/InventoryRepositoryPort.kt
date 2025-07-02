package com.loc.inventory_service.application.port

interface InventoryRepositoryPort {
    fun existsBySkuCodeAndQuantityGreaterThanEqual(skuCode: String, quantity: Int): Boolean
}