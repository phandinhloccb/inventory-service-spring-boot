package com.loc.inventory_service.infrastructure.repository

import com.loc.inventory_service.infrastructure.entity.InventoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaInventoryRepository : JpaRepository<InventoryEntity, Long> {
    fun existsBySkuCodeAndQuantityGreaterThanEqual(skuCode: String, quantity: Int): Boolean
    fun findBySkuCode(skuCode: String): InventoryEntity?
}