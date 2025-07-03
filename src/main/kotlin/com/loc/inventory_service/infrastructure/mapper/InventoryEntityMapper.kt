package com.loc.inventory_service.infrastructure.mapper

import com.loc.inventory_service.domain.model.Inventory
import com.loc.inventory_service.infrastructure.entity.InventoryEntity

fun Inventory.toEntity(): InventoryEntity {
    return InventoryEntity(
        id = this.id,
        skuCode = this.skuCode,
        quantity = this.quantity
    )
}

fun InventoryEntity.toModel(): Inventory {
    return Inventory(
        id = this.id,
        skuCode = this.skuCode,
        quantity = this.quantity
    )
}