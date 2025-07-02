package com.loc.inventory_service.infrastructure.adapter

import com.loc.inventory_service.application.port.InventoryRepositoryPort
import com.loc.inventory_service.infrastructure.repository.JpaInventoryRepository
import org.springframework.stereotype.Component

@Component
class InventoryRepositoryAdapter(
    private val jpaInventoryRepository: JpaInventoryRepository
) : InventoryRepositoryPort {
    override fun existsBySkuCodeAndQuantityGreaterThanEqual(skuCode: String, quantity: Int): Boolean {
        return jpaInventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, quantity)
    }
}