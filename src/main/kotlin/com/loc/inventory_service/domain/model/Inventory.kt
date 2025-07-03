package com.loc.inventory_service.domain.model

data class Inventory(
    val id: Long? = null,
    val skuCode: String,
    val quantity: Int
) {}

