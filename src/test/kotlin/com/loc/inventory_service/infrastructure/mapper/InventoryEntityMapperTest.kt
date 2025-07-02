package com.loc.inventory_service.infrastructure.mapper

import com.loc.inventory_service.domain.model.Inventory
import com.loc.inventory_service.infrastructure.entity.InventoryEntity
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InventoryEntityMapperTest {
    @Test
    fun `should map Inventory to Inventory Entity`() {
        // Given
        val inventory = Inventory(
            id = 1L,
            skuCode = "SKU-TEST",
            quantity = 10
        )

        // When
        val result = inventory.toEntity()

        // THen
        assertEquals(inventory.id, result.id)
        assertEquals(inventory.skuCode, result.skuCode)
        assertEquals(inventory.quantity, result.quantity)
    }

    @Test
    fun `should map Inventory Entity to Inventory`() {
        // Given
        val entity = InventoryEntity(
            id = 1L,
            skuCode = "SKU-TEST",
            quantity = 10
        )

        // When
        val result = entity.toModel()

        // Then
        assertEquals(entity.id, result.id)
        assertEquals(entity.skuCode, result.skuCode)
        assertEquals(entity.quantity, result.quantity)

    }
}