package com.loc.inventory_service.controller.mapper

import com.loc.inventory_service.domain.model.Inventory
import com.loc.inventoryservice.model.StockCheckRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class StockCheckMapperTest {

    @Test
    fun `should map StockCheckRequest to Inventory domain model`() {
        // Given
        val stockCheckRequest = StockCheckRequest(
            skuCode = "IPHONE13-128",
            quantity = 5
        )

        // When
        val inventory = stockCheckRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("IPHONE13-128", inventory.skuCode)
        assertEquals(5, inventory.quantity)
    }

    @Test
    fun `should map StockCheckRequest with zero quantity`() {
        // Given
        val stockCheckRequest = StockCheckRequest(
            skuCode = "ZERO-QTY-SKU",
            quantity = 0
        )

        // When
        val inventory = stockCheckRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("ZERO-QTY-SKU", inventory.skuCode)
        assertEquals(0, inventory.quantity)
    }

    @Test
    fun `should map StockCheckRequest with large quantity`() {
        // Given
        val stockCheckRequest = StockCheckRequest(
            skuCode = "BULK-ITEM",
            quantity = 999999
        )

        // When
        val inventory = stockCheckRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("BULK-ITEM", inventory.skuCode)
        assertEquals(999999, inventory.quantity)
    }

    @Test
    fun `should handle special characters in SKU code`() {
        // Given
        val stockCheckRequest = StockCheckRequest(
            skuCode = "SPECIAL@SKU_123-TEST!",
            quantity = 3
        )

        // When
        val inventory = stockCheckRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("SPECIAL@SKU_123-TEST!", inventory.skuCode)
        assertEquals(3, inventory.quantity)
    }

    @Test
    fun `should handle empty SKU code`() {
        // Given
        val stockCheckRequest = StockCheckRequest(
            skuCode = "",
            quantity = 1
        )

        // When
        val inventory = stockCheckRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("", inventory.skuCode)
        assertEquals(1, inventory.quantity)
    }

    @Test
    fun `should handle long SKU code`() {
        // Given
        val longSkuCode = "A".repeat(100)
        val stockCheckRequest = StockCheckRequest(
            skuCode = longSkuCode,
            quantity = 10
        )

        // When
        val inventory = stockCheckRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals(longSkuCode, inventory.skuCode)
        assertEquals(10, inventory.quantity)
    }

    @Test
    fun `should map StockCheckRequest with minimum valid quantity`() {
        // Given
        val stockCheckRequest = StockCheckRequest(
            skuCode = "MIN-QTY-SKU",
            quantity = 1
        )

        // When
        val inventory = stockCheckRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("MIN-QTY-SKU", inventory.skuCode)
        assertEquals(1, inventory.quantity)
    }

    @Test
    fun `should preserve exact SKU code format`() {
        // Given
        val stockCheckRequest = StockCheckRequest(
            skuCode = "MiXeD-CaSe_SKU123",
            quantity = 7
        )

        // When
        val inventory = stockCheckRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("MiXeD-CaSe_SKU123", inventory.skuCode)
        assertEquals(7, inventory.quantity)
    }

    @Test
    fun `should handle numeric SKU codes`() {
        // Given
        val stockCheckRequest = StockCheckRequest(
            skuCode = "123456789",
            quantity = 2
        )

        // When
        val inventory = stockCheckRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("123456789", inventory.skuCode)
        assertEquals(2, inventory.quantity)
    }

    @Test
    fun `should handle SKU with spaces`() {
        // Given
        val stockCheckRequest = StockCheckRequest(
            skuCode = "SKU WITH SPACES",
            quantity = 4
        )

        // When
        val inventory = stockCheckRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("SKU WITH SPACES", inventory.skuCode)
        assertEquals(4, inventory.quantity)
    }
}