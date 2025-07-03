package com.loc.inventory_service.controller.mapper

import com.loc.inventory_service.domain.model.Inventory
import com.loc.inventoryservice.model.AddInventoryRequest
import com.loc.inventoryservice.model.AddInventoryResponse

fun AddInventoryRequest.toModel(): Inventory {
    return Inventory(
        skuCode = this.skuCode,
        quantity = this.quantity
    )
}

fun Inventory.toResponse(): AddInventoryResponse {
    return AddInventoryResponse(
        skuCode = this.skuCode,
        quantity = this.quantity
    )
}