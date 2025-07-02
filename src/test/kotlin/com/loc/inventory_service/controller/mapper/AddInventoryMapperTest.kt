package com.loc.inventory_service.controller.mapper

import com.loc.inventory_service.domain.model.Inventory
import com.loc.inventoryservice.model.AddInventoryRequest
import com.loc.inventoryservice.model.AddInventoryResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class AddInventoryMapperTest {

    @Test
    fun `should map AddInventoryRequest to Inventory domain model`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "SAMSUNG-S23",
            quantity = 15
        )

        // When
        val inventory = addInventoryRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("SAMSUNG-S23", inventory.skuCode)
        assertEquals(15, inventory.quantity)
    }

    @Test
    fun `should map Inventory domain model to AddInventoryResponse`() {
        // Given
        val inventory = Inventory(
            id = 1L,
            skuCode = "MACBOOK-PRO",
            quantity = 8
        )

        // When
        val response = inventory.toResponse()

        // Then
        assertEquals("MACBOOK-PRO", response.skuCode)
        assertEquals(8, response.quantity)
        // Note: AddInventoryResponse might not have id field based on OpenAPI spec
    }

    @Test
    fun `should map AddInventoryRequest with zero quantity`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "ZERO-STOCK-ITEM",
            quantity = 0
        )

        // When
        val inventory = addInventoryRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("ZERO-STOCK-ITEM", inventory.skuCode)
        assertEquals(0, inventory.quantity)
    }

    @Test
    fun `should map Inventory with null id to response`() {
        // Given
        val inventory = Inventory(
            id = null,
            skuCode = "NULL-ID-SKU",
            quantity = 20
        )

        // When
        val response = inventory.toResponse()

        // Then
        assertEquals("NULL-ID-SKU", response.skuCode)
        assertEquals(20, response.quantity)
    }

    @Test
    fun `should handle special characters in SKU code for request mapping`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "SPECIAL@SKU_456-ADD#TEST",
            quantity = 12
        )

        // When
        val inventory = addInventoryRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("SPECIAL@SKU_456-ADD#TEST", inventory.skuCode)
        assertEquals(12, inventory.quantity)
    }

    @Test
    fun `should handle special characters in SKU code for response mapping`() {
        // Given
        val inventory = Inventory(
            id = 5L,
            skuCode = "RESPONSE@SKU_789-TEST!",
            quantity = 25
        )

        // When
        val response = inventory.toResponse()

        // Then
        assertEquals("RESPONSE@SKU_789-TEST!", response.skuCode)
        assertEquals(25, response.quantity)
    }

    @Test
    fun `should map AddInventoryRequest with large quantity`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "BULK-ADD-ITEM",
            quantity = 1000000
        )

        // When
        val inventory = addInventoryRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("BULK-ADD-ITEM", inventory.skuCode)
        assertEquals(1000000, inventory.quantity)
    }

    @Test
    fun `should map Inventory with large quantity to response`() {
        // Given
        val inventory = Inventory(
            id = 999L,
            skuCode = "LARGE-QTY-RESPONSE",
            quantity = 999999
        )

        // When
        val response = inventory.toResponse()

        // Then
        assertEquals("LARGE-QTY-RESPONSE", response.skuCode)
        assertEquals(999999, response.quantity)
    }

    @Test
    fun `should handle empty SKU code in request mapping`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "",
            quantity = 5
        )

        // When
        val inventory = addInventoryRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("", inventory.skuCode)
        assertEquals(5, inventory.quantity)
    }

    @Test
    fun `should handle empty SKU code in response mapping`() {
        // Given
        val inventory = Inventory(
            id = 100L,
            skuCode = "",
            quantity = 3
        )

        // When
        val response = inventory.toResponse()

        // Then
        assertEquals("", response.skuCode)
        assertEquals(3, response.quantity)
    }

    @Test
    fun `should preserve exact SKU format in request mapping`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "CamelCase_SKU-123",
            quantity = 7
        )

        // When
        val inventory = addInventoryRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("CamelCase_SKU-123", inventory.skuCode)
        assertEquals(7, inventory.quantity)
    }

    @Test
    fun `should preserve exact SKU format in response mapping`() {
        // Given
        val inventory = Inventory(
            id = 42L,
            skuCode = "PascalCase_Response-456",
            quantity = 11
        )

        // When
        val response = inventory.toResponse()

        // Then
        assertEquals("PascalCase_Response-456", response.skuCode)
        assertEquals(11, response.quantity)
    }

    @Test
    fun `should handle numeric SKU codes in request mapping`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "987654321",
            quantity = 6
        )

        // When
        val inventory = addInventoryRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("987654321", inventory.skuCode)
        assertEquals(6, inventory.quantity)
    }

    @Test
    fun `should handle numeric SKU codes in response mapping`() {
        // Given
        val inventory = Inventory(
            id = 777L,
            skuCode = "123456789",
            quantity = 14
        )

        // When
        val response = inventory.toResponse()

        // Then
        assertEquals("123456789", response.skuCode)
        assertEquals(14, response.quantity)
    }

    @Test
    fun `should handle SKU with spaces in request mapping`() {
        // Given
        val addInventoryRequest = AddInventoryRequest(
            skuCode = "SKU WITH SPACES",
            quantity = 9
        )

        // When
        val inventory = addInventoryRequest.toModel()

        // Then
        assertNull(inventory.id)
        assertEquals("SKU WITH SPACES", inventory.skuCode)
        assertEquals(9, inventory.quantity)
    }

    @Test
    fun `should handle SKU with spaces in response mapping`() {
        // Given
        val inventory = Inventory(
            id = 888L,
            skuCode = "RESPONSE WITH SPACES",
            quantity = 13
        )

        // When
        val response = inventory.toResponse()

        // Then
        assertEquals("RESPONSE WITH SPACES", response.skuCode)
        assertEquals(13, response.quantity)
    }
}