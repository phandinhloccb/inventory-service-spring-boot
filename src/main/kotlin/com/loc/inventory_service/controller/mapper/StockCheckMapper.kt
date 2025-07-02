package com.loc.inventory_service.controller.mapper

import com.loc.inventory_service.domain.model.Inventory
import com.loc.inventoryservice.model.StockCheckRequest

fun StockCheckRequest.toModel(): Inventory {
    return Inventory(
        skuCode = this.skuCode,
        quantity = this.quantity
    )
}